package net.poisonlab.gamecast;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

public class Tab1Fr_backup extends Fragment implements ScrollTabHolder, ViewPager.OnPageChangeListener{

    /*
    View view;
    ListView listView;
    PcListItemAdapter1 mAdapter;

    TabHost homePcListTopHeaderTap;
    TabHost homePcListTap;

    private static final int TAB_HEADER_POSITION = 1;

    private static final String NEARPC_TAB = "1";
    private static final String EVENTPC_TAB = "2";

    private static final String NEARPC_TABNAME = "내 주변 PC방";
    private static final String EVENTPC_TABNAME = "이벤트 중 PC방";

    static class DummyContentFactory implements TabHost.TabContentFactory {
        Context mContext;

        public DummyContentFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            TextView tv = new TextView(mContext);
            return tv;
        }
    }
*/

    public static final boolean NEEDS_PROXY = Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 11;

    private View mHeader;
    private View view;

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private HomeListPagerAdapter mPagerAdapter;
    private PcListItemAdapter1 mAdapter;

    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;

    private TextView info;
    private int mLastY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_gamecasthome, container, false);

        ViewPager home_header_vp = (ViewPager) view.findViewById(R.id.home_header_pager);
        HomeHeaderAdaptor hhAdapter = new HomeHeaderAdaptor(inflater);
        home_header_vp.setAdapter(hhAdapter);

        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.home_min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.home_header_height);
        mMinHeaderTranslation = -mMinHeaderHeight;

        mHeader = view.findViewById(R.id.home_list_header);
        //info = (TextView) findViewById(R.id.info);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.home_header_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.home_list_pager);
        mViewPager.setOffscreenPageLimit(2);

        mPagerAdapter = new HomeListPagerAdapter(getChildFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);

        mViewPager.setAdapter(mPagerAdapter);

        mPagerSlidingTabStrip.setShouldExpand(true);
        mPagerSlidingTabStrip.setTextColor(R.color.colorPrimary);
        mPagerSlidingTabStrip.setIndicatorColor(R.color.colorPrimaryDark);
        //mPagerSlidingTabStrip.setTextSize();
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
        mLastY=0;
        /*
        listView = (ListView)view.findViewById(R.id.listView_pclist1);

        addHeader(inflater);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        if(position >= TAB_HEADER_POSITION)
                        {
                            Intent intent = new Intent(getActivity(), PcCafeDetailActivity.class);
                            startActivity(intent);
                        }
                    }
                }
        );

        initData("0");
*/
        return view;

    }
/*
    private void addHeader(LayoutInflater inflater) {

        View headerView = inflater.inflate(R.layout.home_header_vp, null);

        ViewPager home_header_vp = (ViewPager) headerView.findViewById(R.id.home_header_pager);
        HomeHeaderAdaptor hhAdapter = new HomeHeaderAdaptor(inflater);
        home_header_vp.setAdapter(hhAdapter);

        mAdapter = new PcListItemAdapter1(getContext());
        listView.addHeaderView(headerView, null, false);*/
        /*
        //PC방 종류 선택하기 버튼
        View selectorView = inflater.inflate(R.layout.home_selector,null);
        listView.addHeaderView(selectorView);
        */
    /*
        TabHost.TabContentFactory dummyFactory = new DummyContentFactory(getActivity());

        homePcListTap = (TabHost) inflater.inflate(R.layout.home_header_tab, null);
        homePcListTap.setup();

        homePcListTap.addTab(homePcListTap.newTabSpec(NEARPC_TAB).setIndicator(NEARPC_TABNAME).setContent(dummyFactory));
        homePcListTap.addTab(homePcListTap.newTabSpec(EVENTPC_TAB).setIndicator(EVENTPC_TABNAME).setContent(dummyFactory));

        homePcListTap.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (!homePcListTopHeaderTap.getCurrentTabTag().equals(tabId)) {
                    homePcListTopHeaderTap.setCurrentTabByTag(tabId);
                }
            }
        });

        listView.addHeaderView(homePcListTap, null, false);

        homePcListTopHeaderTap = (TabHost)view.findViewById(R.id.home_header_tab);
        homePcListTopHeaderTap.setup();
        homePcListTopHeaderTap.addTab(homePcListTopHeaderTap.newTabSpec(NEARPC_TAB).setIndicator(NEARPC_TABNAME).setContent(dummyFactory));
        homePcListTopHeaderTap.addTab(homePcListTopHeaderTap.newTabSpec(EVENTPC_TAB).setIndicator(EVENTPC_TABNAME).setContent(dummyFactory));
        homePcListTopHeaderTap.setVisibility(View.GONE);

        homePcListTopHeaderTap.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (!homePcListTap.getCurrentTabTag().equals(tabId)) {
                    homePcListTap.setCurrentTabByTag(tabId);
                }
                mAdapter = new PcListItemAdapter1(getContext());
                listView.setAdapter(mAdapter);
                initData(tabId);
            }
        });

        listView.setAdapter(mAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleCount, int totalCount) {
                if (firstVisibleItem < TAB_HEADER_POSITION) {
                    homePcListTopHeaderTap.setVisibility(View.GONE);
                } else {
                    homePcListTopHeaderTap.setVisibility(View.VISIBLE);
                }
            }
        });

    }
*/


    private void initData(String tabId){
        for(int i = 0 ;i <20 ;i++)
        {
            PcListItemData1 d = new PcListItemData1();
            d.smoke = true;
            d.event = true;
            d.cardpay = true;
            d.cafe = true;
            d.name = tabId + i + "번 PC방";
            d.address = tabId + i + "번 주소";
            d.rating = tabId + i + ".5";
            d.reviewCount = String.valueOf(i);
            d.tnailId=R.drawable.zpc;

            mAdapter.add(d);
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
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffsetPixels > 0) {
            int currentItem = mViewPager.getCurrentItem();

            SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
            ScrollTabHolder currentHolder;

            if (position < currentItem) {
                currentHolder = scrollTabHolders.valueAt(position);
            } else {
                currentHolder = scrollTabHolders.valueAt(position + 1);
            }

            if (NEEDS_PROXY) {
                // TODO is not good
                currentHolder.adjustScroll(mHeader.getHeight() - mLastY);
                mHeader.postInvalidate();
            } else {
                currentHolder.adjustScroll((int) (mHeader.getHeight() + mHeader.getTranslationY()));
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        if(NEEDS_PROXY){
            //TODO is not good
            currentHolder.adjustScroll(mHeader.getHeight()-mLastY);
            mHeader.postInvalidate();
        }else{
            currentHolder.adjustScroll((int) (mHeader.getHeight() +mHeader.getTranslationY()));
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            int scrollY = getScrollY(view);
            if(NEEDS_PROXY){
                //TODO is not good
                mLastY=-Math.max(-scrollY, mMinHeaderTranslation);
                info.setText(String.valueOf(scrollY));
                mHeader.scrollTo(0, mLastY);
                mHeader.postInvalidate();
            }else{
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
            }
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        // nothing
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }


    public class HomeListPagerAdapter extends FragmentPagerAdapter {

        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        private final String[] TITLES = {"내 주변 PC방", "이벤트 중인 PC방"};
        private ScrollTabHolder mListener;

        public HomeListPagerAdapter(FragmentManager fm) {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) HomeListTabFragment_Tab1.newInstance(position);

            mScrollTabHolders.put(position, fragment);
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }

            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return mScrollTabHolders;
        }
    }
}
