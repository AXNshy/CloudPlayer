package com.axnshy.cloudmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axnshy.cloudmusic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by axnshy on 16/8/17.
 */
public class DrawMenuAdapter extends BaseAdapter {
    private Context context;
    private String[] menu={"用户管理","退出"};
    private LayoutInflater mInflater;

    private List<String> mItems = new ArrayList<String>(
            Arrays.asList(
                    "用户管理","退出"
            ));

    public DrawMenuAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_menu_item, null);
            holder = new ViewHolder();
            holder.menuItem= (TextView) convertView.findViewById(R.id.tv_drawer_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.menuItem.setText(mItems.get(i));
        return convertView;
    }


    class  ViewHolder {
        TextView menuItem;
    }
}
