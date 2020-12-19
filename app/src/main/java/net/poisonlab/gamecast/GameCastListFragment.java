package net.poisonlab.gamecast;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GameCastListFragment extends Fragment implements LoginFromCastViewInterface{

    ListView listView;
    ViewGroup container;
    Boolean[] selectedArticleGroup2;
    Boolean page_loading;
    Boolean all_items_loaded=false;
    int selectedArticleGroup1;
    int selectedArticleGroup0;
    int currentPage;
    int listLength;
    int selectedPosition = 0;
    GameCastItemAdapter1 mAdapter;
    final static int UPDATE_LIST=1;
    static GameCastItemData1 smData;

    LayoutInflater inflater;

    private ChangeActionBarListener mListener;

    public GameCastListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter = new GameCastItemAdapter1(getContext());
        selectedArticleGroup2 = new Boolean[10];
        this.inflater = inflater;
        this.container = container;
        currentPage = 0;
        page_loading = true;

        if (mListener != null) {

        }

        View view = inflater.inflate(R.layout.fragment_gamecastlist, container, false);
        listView = (ListView) view.findViewById(R.id.listView_tab2);

        View footerView = inflater.inflate(R.layout.gamecastlist_footer, null,false);
        ((TextView)footerView.findViewById(R.id.listfootertext)).setText(getString(R.string.loadingtext));
        footerView.setClickable(false);
        listView.addFooterView(footerView);

        View childView;
        int pos = 0;

        Bundle bundle = this.getArguments();
        selectedArticleGroup0 = bundle.getInt("articleGroup0", 0);
        selectedArticleGroup1 = -1;

        final LinearLayout articleGroup1Layout = (LinearLayout)view.findViewById(R.id.linearlayout_articleGroup1);
        final LinearLayout articleGroup2Container = (LinearLayout)view.findViewById(R.id.linearLayout_articleGroup2_container);
        final LinearLayout articleGroup2Layout = (LinearLayout)view.findViewById(R.id.linearlayout_articleGroup2);


        for(String title : Util.SavedArticleGroup1[selectedArticleGroup0]) {
            if(title == null) break;
            childView = inflater.inflate(R.layout.txt_btn_articlegroup, articleGroup1Layout, false);

            TextView item = (TextView) childView.findViewById(R.id.txt_articleGroupSelect);
            item.setText("#" + title);
            item.setTag(pos);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((int)v.getTag() != selectedArticleGroup1) {
                        selectedArticleGroup1 = (int)v.getTag();
                        clearArticleGroupButtonState(articleGroup1Layout);
                        v.setSelected(true);
                        articleGroup2Container.setVisibility(View.VISIBLE);
                        articleGroup2Layout.removeAllViews();
                        addArticleGroup2Items(articleGroup2Layout);
                    }
                    else{
                        selectedArticleGroup1 = -1;
                        articleGroup2Container.setVisibility(View.GONE);
                        v.setSelected(false);
                    }
                    updateGameCastItems(true);
                }
            });

            pos++;
            articleGroup1Layout.addView(item);
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(page_loading == false && firstVisibleItem+visibleItemCount < listLength)
                    {
                            currentPage++;
                            page_loading = true;
                            updateGameCastItems(false);
                    }
                    else if(page_loading == false && firstVisibleItem+visibleItemCount >= listLength)
                    {
                        ((TextView)listView.findViewById(R.id.listfootertext)).setText("모든 캐스트를 불러왔습니다.");
                    }
                }
            }
        });

        listView.setAdapter(mAdapter);
        updateGameCastItems(true);

        return view;

    }

    void updateGameCastItems(final Boolean isRefreshItems){
        ((TextView)listView.findViewById(R.id.listfootertext)).setText(R.string.loadingtext);
        if(isRefreshItems == true)
        {
            currentPage = 0;
        }
        AsyncHttpClient client = new AsyncHttpClient();

        int preparedArticleGroup2 = 0;
        for(int i = 0 ; i < selectedArticleGroup2.length; i++)
        {
            if(selectedArticleGroup2[i] != null && selectedArticleGroup2[i]) {
                preparedArticleGroup2 += Math.pow(2, i);
            }
        }

        String webURL = "http://" + MainActivity.currentServer + "/admin/gamecast?articleGroup0=" + selectedArticleGroup0
                + "&articleGroup1=" + selectedArticleGroup1 + "&articleGroup2=" + preparedArticleGroup2
                + "&page=-1";

        client.get(webURL, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        listLength = response.length();
                    }
                });

        webURL = "http://" + MainActivity.currentServer + "/admin/gamecast?articleGroup0=" + selectedArticleGroup0
                        + "&articleGroup1=" + selectedArticleGroup1 + "&articleGroup2=" + preparedArticleGroup2
                        + "&page=" + currentPage;

        client.get(webURL, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                int i;

                if(isRefreshItems == true)
                {
                    mAdapter = new GameCastItemAdapter1(getContext());
                }

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

                        if(MainActivity.logged == true) {

                            String likeURL = "http://" + MainActivity.currentServer + "/admin/likeit?articleId=" + d.articleId
                                    + "&userPK=" + MainActivity.userPK;

                            (new AsyncHttpClient()).get(likeURL,null,new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
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

                    if(isRefreshItems == true)
                    {
                        listView.setAdapter(mAdapter);
                    }

                } catch(JSONException e){
                    e.printStackTrace();
                }

                bindClickEvent();
                mAdapter.notifyDataSetChanged();
                page_loading = false;
            }
        });


    }

    private void addArticleGroup2Items(LinearLayout articleGroup2Layout) {
        int pos = 0;
        View childView;

        int amount = 0;
        for(String title : Util.SavedArticleGroup2[selectedArticleGroup0][selectedArticleGroup1]) {
            if(title == null) break;
            amount++;
        }
        selectedArticleGroup2 = new Boolean[amount];

        for(String title : Util.SavedArticleGroup2[selectedArticleGroup0][selectedArticleGroup1]) {
            if(title == null) break;
            childView = inflater.inflate(R.layout.txt_btn_articlegroup, articleGroup2Layout, false);

            TextView item = (TextView) childView.findViewById(R.id.txt_articleGroupSelect);
            item.setText("#" + title);
            item.setTag(pos);
            selectedArticleGroup2[pos] = false;

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedArticleGroup2[(int)v.getTag()])
                    {
                        selectedArticleGroup2[(int)v.getTag()] = false;
                        v.setSelected(false);
                    }
                    else
                    {
                        selectedArticleGroup2[(int)v.getTag()] = true;
                        v.setSelected(true);
                    }

                    updateGameCastItems(true);
                }
            });

            pos++;
            articleGroup2Layout.addView(item);
        }
    }


    void clearArticleGroupButtonState(LinearLayout parentLayout){
        for(int i = 0 ;i < parentLayout.getChildCount(); i++){
            (parentLayout.getChildAt(i)).setSelected(false);
        }
    }

    private void bindClickEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //GameCastItemAdapter1 mAdapter = (GameCastItemAdapter1)parent.getAdapter();

                smData = (GameCastItemData1) mAdapter.getItem(position);
                selectedPosition = position;
                openViewActivity(getContext());
            }
        });
    }

    public void openViewActivity(Context mContext){

        String webURL = smData.webURL;
        int articleId = smData.articleId;

        Intent intent = new Intent(mContext, GameCastViewActivity.class);
        intent.putExtra("webURL",webURL);
        intent.putExtra("articleId", articleId);
        intent.putExtra("likeItCount", smData.likeItCount);
        intent.putExtra("commentCount", smData.commentCount);
        intent.putExtra("viewCount", smData.viewCount);
        intent.putExtra("likeIt", smData.likeIt);

        startActivityForResult(intent,UPDATE_LIST);
    }

    @Override
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == UPDATE_LIST)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                System.out.println("RESULT_OK 호출");
                if (data != null) {
                    GameCastItemData1 mData = (GameCastItemData1) mAdapter.getItem(selectedPosition);
                    mData.likeItCount = data.getIntExtra("likeItCount",mData.likeItCount);
                    mData.viewCount = data.getIntExtra("viewCount",mData.viewCount);
                    mData.commentCount = data.getIntExtra("commentCount",mData.commentCount);
                    mData.likeIt = data.getBooleanExtra("likeIt",false);

                    mAdapter.updateItemOnPosition(mData,selectedPosition);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {

        }

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
