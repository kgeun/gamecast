package net.poisonlab.gamecast;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KKLee on 16. 6. 11..
 */
public class HomeHeaderAdaptor extends PagerAdapter {

    LayoutInflater inflater;
    private int count;
    public HomeHeaderAdaptor(LayoutInflater inflater) {
        this.inflater = inflater;
        count = 3;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

            View view=null;

            view= inflater.inflate(R.layout.home_header_ct, null);

            TextView title = (TextView)view.findViewById(R.id.txt_homeheader_title);
            ImageView img= (ImageView)view.findViewById(R.id.img_pcdetail_tnil);
            TextView desc = (TextView)view.findViewById(R.id.txt_homeheader_desc);

            container.addView(view);

            return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
