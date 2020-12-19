package net.poisonlab.gamecast;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KKLee on 16. 7. 12..
 */
public class GCCommentItemAdapter extends BaseAdapter {

    ArrayList<GCCommentItemData> items = new ArrayList<GCCommentItemData>();
    Context mContext;

    public GCCommentItemAdapter(Context context) {
        mContext = context;
    }

    public void add(GCCommentItemData item)
    {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<GCCommentItemData> items)
    {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void removeitem(int position){
        items.remove(position);
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
        GCCommentItemView v;

        if (convertView == null) {
            v = new GCCommentItemView(mContext);
        } else {
            v = (GCCommentItemView)convertView;
        }

        v.setGCCommentItemData(items.get(position));
        return v;
    }

    void clear()
    {
        items.clear();
    }
}
