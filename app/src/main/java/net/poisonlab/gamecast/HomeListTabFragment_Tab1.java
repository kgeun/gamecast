package net.poisonlab.gamecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by KKLee on 16. 6. 30..
 */
public class HomeListTabFragment_Tab1 extends ScrollTabHolderFragment{


    private static final String ARG_POSITION = "position";

    private ListView mListView;
    private ArrayList<String> mListItems;

    private int mPosition;

    private PcListItemAdapter1 MPcListAdapter;

    public static Fragment newInstance(int position) {
        HomeListTabFragment_Tab1 f = new HomeListTabFragment_Tab1();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        MPcListAdapter = new PcListItemAdapter1(getActivity());

        for(int i = 0 ;i <20 ;i++)
        {
            PcListItemData1 d = new PcListItemData1();
            d.smoke = true;
            d.event = true;
            d.cardpay = true;
            d.cafe = true;
            d.name = i + "번 PC방";
            d.address = i + "번 주소";
            d.rating = i + ".5";
            d.reviewCount = String.valueOf(i);
            d.tnailId=R.drawable.zpc;

            MPcListAdapter.add(d);
        }

        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.pager_fragment_list, null);

        mListView = (ListView) v.findViewById(R.id.listView);

        View placeHolderView = inflater.inflate(R.layout.homeheader_page_placeholder, mListView, false);
        placeHolderView.setBackgroundColor(0xFFFFFFFF);

        mListView.addHeaderView(placeHolderView);

        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        Intent intent = new Intent(getActivity(), PcCafeDetailActivity.class);
                            startActivity(intent);
                    }
                }
        );

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnScrollListener(new OnScroll());
        mListView.setAdapter(MPcListAdapter);

        if(PcCafeDetailActivity.NEEDS_PROXY){//in my moto phone(android 2.1),setOnScrollListener do not work well
            mListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mScrollTabHolder != null)
                        mScrollTabHolder.onScroll(mListView, 0, 0, 0, mPosition);
                    return false;
                }
            });
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        mListView.setSelectionFromTop(1, scrollHeight);

    }

    public class OnScroll implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (mScrollTabHolder != null)
                mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
        }

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount, int pagePosition) {
    }

}