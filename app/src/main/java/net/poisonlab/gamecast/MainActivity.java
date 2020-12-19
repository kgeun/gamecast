package net.poisonlab.gamecast;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements ChangeActionBarListener, NavigationView.OnNavigationItemSelectedListener {


    private static final String HOME_TAG = "home";
    private static final String LOCATION_TAG ="location";
    private static final String AROUND_TAG = "around";
    private static final String GAMECAST_TAG ="gamecast";
    private static final String EVENT_TAG = "event";
    private static final int CHK_LOGIN = 0;
    private String loginMethod;

    private FragmentTabHost mTabHost;
    final MainActivity thisActivity = this;
    static NavigationView navigationView;
    static DrawerLayout drawer;
    static Boolean logged;
    static String userName, userPK;
    public static String currentServer ="10.0.3.2:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logged = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCustomToolBar();
        InitView();

        applyLoginResult("","","");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://" + MainActivity.currentServer + "/admin/articlegroup", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                int i, j, k;

                try {
                    for (i = 0; i < response.length(); i++) {

                        JSONObject categoryObject1 = (JSONObject) response.get(i);
                        JSONArray gameArray1 = categoryObject1.getJSONArray("submenu");
                        for (j = 0; j < gameArray1.length(); j++) {

                            JSONObject categoryObject2 = (JSONObject) gameArray1.get(j);
                            Util.SavedArticleGroup1[i][j] = categoryObject2.getString("title");

                            JSONArray gameArray2 = categoryObject2.getJSONArray("submenu");

                            for (k = 0; k < gameArray2.length(); k++) {
                                JSONObject categoryObject3 = (JSONObject) gameArray2.get(k);
                                Util.SavedArticleGroup2[i][j][k] = categoryObject3.getString("title");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }


    @Override
    protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHK_LOGIN)
        {
            if(resultCode == RESULT_OK)
            {
                if (data != null) {
                    logged = data.getBooleanExtra("logged", false);
                    userPK = data.getStringExtra("userPK");
                    userName = data.getStringExtra("nickname");
                    applyLoginResult(userName,
                            userPK,
                            data.getStringExtra("thumbnail_image"));
                    if(GameCastViewActivity.loginFromCastView) {
                        GameCastItemData1 smData = GameCastListFragment.smData;

                        String webURL = smData.webURL;
                        int articleId = smData.articleId;

                        Intent intent = new Intent(this, GameCastViewActivity.class);
                        intent.putExtra("webURL",webURL);
                        intent.putExtra("articleId", articleId);
                        intent.putExtra("likeItCount", smData.likeItCount);
                        intent.putExtra("commentCount", smData.commentCount);
                        intent.putExtra("viewCount", smData.viewCount);
                        intent.putExtra("likeIt", smData.likeIt);

                        startActivityForResult(intent,GameCastListFragment.UPDATE_LIST);
                    }
                }
            }
        }
    }


    synchronized void applyLoginResult(String userName, String userPK, String tnilURL) {

        if (logged) {
            this.userName = userName;
            navigationView.getMenu().clear();
            navigationView.removeHeaderView(navigationView.getHeaderView(0));

            View headerView = getLayoutInflater().inflate(R.layout.nav_header_logged, null);
            ((TextView) headerView.findViewById(R.id.txt_nav_userName)).setText(userName + "님 반갑습니다.");
            ((TextView) headerView.findViewById(R.id.txt_nav_email)).setText(userPK);

            navigationView.addHeaderView(headerView);
            navigationView.inflateMenu(R.menu.activity_main_drawer_logged);

        } else {
            navigationView.getMenu().clear();
            navigationView.removeHeaderView(navigationView.getHeaderView(0));

            View headerView = getLayoutInflater().inflate(R.layout.nav_header_unlogged, null);
            navigationView.addHeaderView(headerView);
            navigationView.inflateMenu(R.menu.activity_main_drawer_unlogged);
        }
    }


    private void setCustomToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void InitView() {
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(HOME_TAG),
                R.drawable.tab_indicator_gen, "게임캐스트 홈", R.drawable.tabiconhome), Tab1Container.class, null);
        mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(AROUND_TAG),
                R.drawable.tab_indicator_gen, "오버워치", R.drawable.tabiconoverwatch_unselected), Tab2Container.class, null);
        mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(GAMECAST_TAG),
                R.drawable.tab_indicator_gen, "리그 오브 레전드", R.drawable.tabiconlol_unselected), Tab3Container.class, null);
        mTabHost.addTab(setIndicator(MainActivity.this, mTabHost.newTabSpec(LOCATION_TAG),
                R.drawable.tab_indicator_gen, "서든어택", R.drawable.sudden_unselected), Tab4Container.class, null);
        TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.txt_tabtxt);
        ImageView img = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.img_tabtxt);
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        img.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setTabColor(mTabHost);
            }
        });


    }

    public void setTabColor(FragmentTabHost tabhost) {

        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(R.id.txt_tabtxt);
            ImageView img = (ImageView) tabhost.getTabWidget().getChildAt(i).findViewById(R.id.img_tabtxt);

            tv.setTextColor(getResources().getColor(R.color.tabspecitem));
            img.clearColorFilter();

        }

        tabhost.getTabWidget().setCurrentTab(0);
        TextView tv = (TextView) tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).findViewById(R.id.txt_tabtxt);
        ImageView img = (ImageView) tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).findViewById(R.id.img_tabtxt);
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        img.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        View v = LayoutInflater.from(thisActivity).inflate(R.layout.tab_item, null);
        v.setBackgroundResource(R.drawable.tab_indicator_gen);
    }

    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec,
                                         int resid, String string, int genresIcon)
    {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
        v.setBackgroundResource(resid);
        TextView tv = (TextView) v.findViewById(R.id.txt_tabtxt);
        ImageView img = (ImageView) v.findViewById(R.id.img_tabtxt);

        tv.setText(string);
        img.setImageResource(genresIcon);

        return spec.setIndicator(v);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login_fb) {
            loginMethod = "Facebook";
            Intent intent = new Intent(this, FacebookLoginActivity.class);
            startActivityForResult(intent, CHK_LOGIN);
        } else if (id == R.id.nav_login_naver) {
            loginMethod = "Naver";
            Intent intent = new Intent(this, NaverLoginActivity.class);
            startActivityForResult(intent, CHK_LOGIN);
        } else if (id == R.id.nav_signout) {
            if(loginMethod.equals("Facebook")){
                LoginManager.getInstance().logOut();
                logged = false;
                finish();
                startActivity(getIntent());
            }
            else if(loginMethod.equals("Naver")){
                OAuthLogin mOAuthLoginInstance = OAuthLogin.getInstance();
                mOAuthLoginInstance.init(this, getString(R.string.naver_oauth_client_id),
                        getString(R.string.naver_oauth_client_secret), getString(R.string.naver_oauth_client_name));
                mOAuthLoginInstance.logout(this);
                logged = false;
                finish();
                startActivity(getIntent());
            }

        } else if (id == R.id.nav_companyinfo) {
            Intent intent = new Intent(this, CompanyInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void ChangeActionBarByResource(int id)
    {
        ActionBar actionBar = getSupportActionBar();

        // Set custom view layout
        /*
        View mCustomView = getLayoutInflater().inflate(R.layout.actionbar_fr1, null);
        ImageView img = (ImageView) mCustomView.findViewById(R.id.actionbar_logo);
        img.setBackgroundResource(id);
        actionBar.setCustomView(mCustomView);
*/

        /*

        // Set no padding both side

        Toolbar parent = (Toolbar) mCustomView.getParent(); // first get parent toolbar of current action bar

        parent.setContentInsetsAbsolute(0, 0);              // set padding programmatically to 0dp

        // Set actionbar background image
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));

        // Set actionbar layout layoutparams

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(mCustomView, params);

        */




    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

}
