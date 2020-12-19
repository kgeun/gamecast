package net.poisonlab.gamecast;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class GameCastHomeFragment extends Fragment {

    private ChangeActionBarListener mListener;
    public final static int LOOPS = 1000;
    public CarouselPagerAdapter mAdapter;
    public ViewPager mViewPager;
    public static int count = 10; //ViewPager items size
    public static int FIRST_PAGE = 10;


    public GameCastHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        if (mListener != null) {
            mListener.ChangeActionBarByResource(R.drawable.gamecastlogo);
        }

        View view = inflater.inflate(R.layout.fragment_gamecasthome, container, false);

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
/*
        ViewPager home_header_vp = (ViewPager) view.findViewById(R.id.home_header_pager);
        HomeHeaderAdaptor hhAdapter = new HomeHeaderAdaptor(inflater);
        home_header_vp.setPageMargin(-20);
        home_header_vp.setAdapter(hhAdapter);
        home_header_vp.setOffscreenPageLimit(3);
*/
        mViewPager = (ViewPager) view.findViewById(R.id.bestViewPager);

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 6) *2);
        mViewPager.setPageMargin(-pageMargin);

        mAdapter = new CarouselPagerAdapter(this, getChildFragmentManager());

        (new AsyncHttpClient()).get("http://" + MainActivity.currentServer + "/admin/bestgamecast", null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            int i;

            try{
                for(i=0; i<response.length(); i++){

                    JSONObject articleObject = (JSONObject) response.get(i);

                    final GameCastItemData1 d = new GameCastItemData1();

                    d.articleGroup0 = articleObject.getInt("article_group0");
                    d.articleGroup1 = articleObject.getInt("article_group1");
                    d.articleGroup2 = articleObject.getInt("article_group2");
                    d.writeDate = articleObject.getString("write_date");
                    d.articleSource = articleObject.getString("article_source");
                    d.articleTitle = articleObject.getString("article_title");
                    d.commentCount = articleObject.getInt("comment_count");;
                    d.likeItCount = articleObject.getInt("like_it_count");
                    d.viewCount = articleObject.getInt("view_count");
                    d.writer = articleObject.getString("writer");
                    d.imageURL = articleObject.getString("imageurl");
                    d.articleId = articleObject.getInt("article_id");
                    d.webURL = articleObject.getString("weburl");

                    System.out.println("articleTitle : " + d.articleTitle);

                    if(MainActivity.logged == true) {

                        String likeURL = "http://" + MainActivity.currentServer + "/admin/likeit?articleId=" + d.articleId
                                + "&userPK=" + MainActivity.userPK;

                        (new AsyncHttpClient()).get(likeURL,null,new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                                if(response.length() > 0){
                                    d.likeIt = true;
                                }
                                else {
                                    d.likeIt = false;
                                }
                                mAdapter.add(d);
                            }
                        });

                    }
                    else{
                        mAdapter.add(d);
                    }

                }

                mViewPager.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mViewPager.addOnPageChangeListener(mAdapter);

                // Set current item to the middle page so we can fling to both
                // directions left and right
                mViewPager.setCurrentItem(FIRST_PAGE);
                mViewPager.setOffscreenPageLimit(3);

            } catch(JSONException e){
                e.printStackTrace();
            }

            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ChangeActionBarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
