package com.test.sample.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.ramenapp.R;
import com.test.sample.config.Config;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuAdapter extends BaseAdapter{

    private ArrayList<HashMap<String, String>> list;
    private FragmentActivity mContext;

    public MenuAdapter(FragmentActivity context, ArrayList<HashMap<String, String>> menuArrayList) {
        this.list = menuArrayList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_menu_item,parent,false);
            viewHolder = new MenuAdapter.ViewHolder();

            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.image = (ImageView)convertView.findViewById(R.id.image);
            viewHolder.price = (TextView)convertView.findViewById(R.id.price);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(list.get(position).get("name"));
        Glide.with(mContext).load(Config.GET_MENU_URL + list.get(position).get("url")).into(viewHolder.image);

        viewHolder.price.setText(list.get(position).get("price"));

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        ImageView image;
        TextView price;
    }
}
