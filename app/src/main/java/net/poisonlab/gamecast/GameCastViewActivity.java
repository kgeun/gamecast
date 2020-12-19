package net.poisonlab.gamecast;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

import cz.msebera.android.httpclient.Header;

/**
 * Created by KKLee on 16. 7. 6..
 */
public class GameCastViewActivity extends Activity {

    AdView adView;
    String twebURL;
    int commentCount, likeItCount, viewCount;
    int articleId, articleGroup0;
    String commentURL;
    Intent intent;
    EditText comment_text;
    boolean likeIt;
    Context mContext;
    LayoutInflater inflater;
    static boolean loginFromCastView = false;


    AsyncHttpClient client;
    ListView listView;

    public static int dpToPx(int dp)  { return (int) (dp * Resources.getSystem().getDisplayMetrics().density); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamecast_ctntview);
        mContext = this;
        inflater = getLayoutInflater();

        final SlidingUpPanelLayout panel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        final int currentPanelHeight = panel.getPanelHeight();

        client = new AsyncHttpClient();
        adView = (AdView)findViewById(R.id.adView2);

        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice("4D5C7E9447E0523387918E6D62D697D4")  // Will your Test device id.
        //        .build();

        //AdRequest adRequest =new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("자신의 해당 ID").build();

        AdRequest adRequest =new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        articleId = (int)intent.getSerializableExtra("articleId");
        commentURL = "http://" + MainActivity.currentServer + "/admin/gccomment";
        twebURL = (String)intent.getSerializableExtra("webURL");
        likeItCount = (int)intent.getSerializableExtra("likeItCount");
        commentCount = (int)intent.getSerializableExtra("commentCount");
        viewCount = (int)intent.getSerializableExtra("viewCount");
        likeIt = (boolean)intent.getSerializableExtra("likeIt");

        comment_text = (EditText) findViewById(R.id.edittxt_comment);
        final EditText commentText = comment_text;

        LinearLayout editTextBox = (LinearLayout) findViewById(R.id.linearlayout_edittextbox);
        editTextBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(commentText.hasFocus())
                {
                    showSoftKeyboard(commentText);
                }
                else
                {
                    commentText.requestFocus();
                }

                return true;
            }
        });
        commentText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    showSoftKeyboard(v);  // 프로그램적으로 keyboard 를 띄운다.
                }
                else
                {
                    hideSoftKeyboard();
                }
            }
        });

        //articleId로 Weburl, 댓글 갯수 등 가져오기

        getArticle();
        setValueToView();

        WebView ctntWebView = (WebView) findViewById(R.id.webview_gamecast_ctnt);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.webProgressBar);

        progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        ctntWebView.loadUrl(twebURL);
        ctntWebView.setWebChromeClient(new CustomWebChromeClient(this));
        ctntWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            };

            public void onPageStarted(WebView view, String url,
                                      android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            };

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            };

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(GameCastViewActivity.this, "로딩오류" + description,
                        Toast.LENGTH_SHORT).show();
            };
        });
        ctntWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        ctntWebView.getSettings().setJavaScriptEnabled(true);
        ctntWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        ctntWebView.getSettings().setSupportMultipleWindows(true);
        ctntWebView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK);
        ctntWebView.getSettings().setAppCacheEnabled(true);
        ctntWebView.getSettings().setDomStorageEnabled(true);
        ctntWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //ctntWebView.getSettings().setUseWideViewPort(true);
        ctntWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //ctntWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        ctntWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= 19) {
            // chromium, enable hardware acceleration
            ctntWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            ctntWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        listView = (ListView) findViewById(R.id.listView_comment);
        client = new AsyncHttpClient();

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return false;
            }
        });

        ((LinearLayout)findViewById(R.id.linearlayout_nologin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFromCastView = true;
                finish();
                MainActivity.drawer.openDrawer(Gravity.LEFT);
            }
        });

        ((ImageView)findViewById(R.id.imgbtn_ctnt_icon_likeit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String likeURL = "http://" + MainActivity.currentServer + "/admin/likeit";

                if(likeIt){
                    ((ImageView)v).clearColorFilter();
                    ((ImageView) findViewById(R.id.img_ctnt_icon_likeit)).clearColorFilter();
                    Toast.makeText(mContext,"좋아요를 취소했습니다", Toast.LENGTH_SHORT).show();

                    RequestParams params = new RequestParams();
                    params.put("articleId", articleId);
                    params.put("userPK", MainActivity.userPK);
                    (new AsyncHttpClient()).delete(likeURL, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            getArticle();
                            likeIt = false;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(mContext,"좋아요 취소에 실패했네요", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    ((ImageView)v).setColorFilter(getResources()
                            .getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                    ((ImageView) findViewById(R.id.img_ctnt_icon_likeit)).setColorFilter(getResources()
                            .getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

                    Toast.makeText(mContext,"좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();

                    RequestParams params = new RequestParams();
                    params.put("articleId", articleId);
                    params.put("userPK", MainActivity.userPK);

                    (new AsyncHttpClient()).post(likeURL, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.println("좋아요성공");
                            getArticle();
                            likeIt = true;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(mContext,"좋아요에 실패했네요", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        if(MainActivity.logged == false)
        {
            findViewById(R.id.linearlayout_nologin).setVisibility(View.VISIBLE);
            findViewById(R.id.linearlayout_commentwrite).setVisibility(View.GONE);
        }
        else
        {
            findViewById(R.id.linearlayout_nologin).setVisibility(View.GONE);
            findViewById(R.id.linearlayout_commentwrite).setVisibility(View.VISIBLE);
        }

        refreshComment(false,0);

        ImageButton sendbtn = (ImageButton) findViewById(R.id.btn_commentsend);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(commentText.getText() == null || commentText.getText().equals(""))
                {
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("articleId", articleId);
                params.put("content", commentText.getText());
                params.put("userName", MainActivity.userName);
                params.put("userPK", MainActivity.userPK);

                client.post(commentURL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        refreshComment(true,0);
                        getArticle();
                        commentText.setText("");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("이런 실패했네요");
                    }
                });
            }
        });


        //조회수 올리기
        RequestParams params = new RequestParams();
        params.put("articleId", articleId);

        client.post("http://" + MainActivity.currentServer + "/admin/addviewcount", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("이런 실패했네요");
            }
        });

    }

    private void refreshComment(final Boolean scroll,final int position) {

        final Activity context = this;

        client.get(commentURL + "?articleId=" + articleId, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                int i;

                final GCCommentItemAdapter mAdapter = new GCCommentItemAdapter(context);

                try{
                    if(response.length() > 0) {
                        if(listView.findViewById(R.id.listfooter)!=null)
                            listView.findViewById(R.id.listfooter).setVisibility(View.GONE);

                        for (i = 0; i < response.length(); i++) {

                            JSONObject articleObject = (JSONObject) response.get(i);

                            GCCommentItemData d = new GCCommentItemData();

                            d.content = articleObject.getString("content");
                            d.commentId = articleObject.getInt("comment_id");
                            d.userName = articleObject.getString("user_name");
                            d.writeDate = articleObject.getString("write_date");
                            d.userPK = articleObject.getString("userpk");

                            mAdapter.add(d);
                        }
                    }
                    else {
                        View footerView = inflater.inflate(R.layout.gamecastlist_footer, null,false);
                        ((TextView)footerView.findViewById(R.id.listfootertext)).setText("첫 댓글의 주인공이 되세요!");
                        footerView.setClickable(false);
                        listView.addFooterView(footerView);
                    }


                } catch(JSONException e){
                    e.printStackTrace();
                }

                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                bindCommentClickEvent(mAdapter);

                if(scroll) {

                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            try {
                                if(position == 0) {
                                    listView.setSelection(mAdapter.getCount() - 1);
                                }
                                else {
                                    listView.setSelection(position);
                                }

                                //listView.smoothScrollToPosition(mAdapter.getCount()-1);
                            } catch (Exception e) {}
                        }
                    }, 100);
                }

            }
        });

    }

    private void bindCommentClickEvent(final GCCommentItemAdapter mAdapter) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final GCCommentItemData mData = (GCCommentItemData) mAdapter.getItem(position);
                final int pos = position;
                if(mData.userPK.equals(MainActivity.userPK))
                {
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("댓글 삭제");
                    ab.setMessage("작성하신 댓글을 삭제하시겠습니까?");
                    ab.setCancelable(true);

                    ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String commentDelURL = "http://" + MainActivity.currentServer + "/admin/gccomment?commentId="+ mData.commentId
                                    + "&articleId=" + articleId;

                            (new AsyncHttpClient()).delete(commentDelURL, null, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    Toast.makeText(mContext,"댓글을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                    refreshComment(true,pos);
                                    getArticle();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast.makeText(mContext,"댓글삭제에 실패했네요.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    ab.create();
                    ab.show();
                }

            }
        });
    }

    private void getArticle() {
        String articleURL = "http://" + MainActivity.currentServer + "/admin/gamecast?articleId=" + articleId;

        client.get(articleURL, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try{
                    JSONObject articleObject = (JSONObject)response.get(0);

                    GameCastItemData1 cData = new GameCastItemData1();

                    cData.webURL = articleObject.getString("weburl");
                    cData.viewCount = articleObject.getInt("view_count");
                    cData.likeItCount = articleObject.getInt("like_it_count");
                    cData.commentCount = articleObject.getInt("comment_count");

                    likeItCount = cData.likeItCount;
                    commentCount = cData.commentCount;
                    viewCount = cData.viewCount;

                    if(MainActivity.logged == true) {

                        String likeURL = "http://" + MainActivity.currentServer + "/admin/likeit?articleId=" + articleId
                                + "&userPK=" + MainActivity.userPK;

                        (new AsyncHttpClient()).get(likeURL,null,new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                if(response.length() > 0){
                                    likeIt = true;
                                }
                                else {
                                    likeIt = false;
                                }
                                setValueToView();
                            }
                        });

                    }
                    else{
                        setValueToView();
                    }

                    intent = new Intent();
                    intent.putExtra("likeItCount", likeItCount);
                    intent.putExtra("commentCount",commentCount);
                    intent.putExtra("viewCount",viewCount);
                    intent.putExtra("likeIt",likeIt);
                    setResult(RESULT_OK,intent);

                } catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void setValueToView() {
        ((TextView)findViewById(R.id.txt_ctnt_commentCount)).setText("댓글 " + commentCount);
        ((TextView)findViewById(R.id.txt_ctnt_likeitCount)).setText("좋아요 " + likeItCount);
        if(likeIt) {
            ((ImageView) findViewById(R.id.img_ctnt_icon_likeit)).setColorFilter(getResources()
                    .getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            ((ImageView) findViewById(R.id.imgbtn_ctnt_icon_likeit)).setColorFilter(getResources()
                    .getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }
        else{
            ((ImageView) findViewById(R.id.img_ctnt_icon_likeit)).clearColorFilter();
            ((ImageView) findViewById(R.id.imgbtn_ctnt_icon_likeit)).clearColorFilter();
        }
    }

    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.resume();
    }


    @Override
    public void onBackPressed() {
        loginFromCastView = false;
        super.onBackPressed();
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
