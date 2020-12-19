package net.poisonlab.gamecast;

/**
 * Created by KKLee on 16. 7. 11..
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.9f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private GameCastHomeFragment context;
    private FragmentManager fragmentManager;
    private float scale;
    ArrayList<GameCastItemData1> items = new ArrayList<GameCastItemData1>();

    public void add(GameCastItemData1 item)
    {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<GameCastItemData1> items)
    {
        this.items.addAll(items);
        notifyDataSetChanged();
    }


    public CarouselPagerAdapter(GameCastHomeFragment context, FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        try {
            if (position == GameCastHomeFragment.FIRST_PAGE)
                scale = BIG_SCALE;
            else
                scale = SMALL_SCALE;

            position = position % GameCastHomeFragment.count;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CarouselPagerItemFragment.newInstance(context, scale, items.get(position));
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = GameCastHomeFragment.count * GameCastHomeFragment.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);

                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("ConstantConditions")
    private CarouselLinearLayout getRootView(int position) {
        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.mViewPager.getId() + ":" + position;
    }
}