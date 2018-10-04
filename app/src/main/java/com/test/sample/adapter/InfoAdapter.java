package com.test.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.ramenapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoAdapter extends BaseAdapter{

    private final ArrayList<HashMap<String, String>> list;

    public InfoAdapter(ArrayList<HashMap<String, String>> infoArrayList) {
        this.list = infoArrayList;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_info_item,parent,false);
            TextView title = (TextView)convertView.findViewById(R.id.title);
            TextView body = (TextView)convertView.findViewById(R.id.body);

            if (list != null) {
                title.setText(list.get(position).get("title"));
                body.setText(list.get(position).get("body"));
            }
        } else {

        }
        return convertView;
    }
}
