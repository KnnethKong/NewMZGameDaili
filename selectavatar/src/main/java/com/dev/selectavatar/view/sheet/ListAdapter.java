package com.dev.selectavatar.view.sheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.selectavatar.R;


/**
 * Created by Clevo on 2016/6/7.
 */
public class ListAdapter extends BaseAdapter {

    Context context;
    String title[];

    public ListAdapter(Context context, String[] title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sheet, parent, false);
            holder = new ViewHolder();
            holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvItem.setText(title[position]);
        return convertView;
    }

    public static class ViewHolder {
        TextView tvItem;
    }
}
