package net.poisonlab.gamecast;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class PcListItemAdapter1 extends BaseAdapter {

    ArrayList<PcListItemData1> items = new ArrayList<PcListItemData1>();
    Context mContext;

    public PcListItemAdapter1(Context context) {
        mContext = context;
    }

    public void add(PcListItemData1 item)
    {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<PcListItemData1> items)
    {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PcListItemView1 v;

        if (convertView == null) {
            v = new PcListItemView1(mContext);
        } else {
            v = (PcListItemView1)convertView;
        }

        v.setPcListItemData1(items.get(position));
        return v;
    }
}
