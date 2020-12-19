package net.poisonlab.gamecast;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameCastItemAdapter1 extends BaseAdapter {

    ArrayList<GameCastItemData1> items = new ArrayList<GameCastItemData1>();
    Context mContext;

    public GameCastItemAdapter1(Context context) {
        mContext = context;
    }

    public void updateItemOnPosition(GameCastItemData1 item, int position)
    {
        items.set(position,item);
        notifyDataSetChanged();
    }

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
        GameCastItemView1 v;

        if (convertView == null) {
            v = new GameCastItemView1(mContext);
        } else {
            v = (GameCastItemView1)convertView;
        }

        v.setGameCastItemData1(items.get(position));
        return v;
    }

    void clear()
    {
        items.clear();
    }

}
