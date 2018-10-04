package com.test.sample.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.ramenapp.R;
import com.test.sample.database.Ticket;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class TicketAdapter extends RealmBaseAdapter<Ticket> {


    private static class ViewHolder{
        TextView title;
        TextView body;
        TextView price;
    }

    public TicketAdapter(@Nullable OrderedRealmCollection<Ticket> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView)convertView.findViewById(R.id.ticket_title);
            viewHolder.body = (TextView)convertView.findViewById(R.id.ticket_body);
            viewHolder.price = (TextView)convertView.findViewById(R.id.ticket_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Ticket ticket = adapterData.get(position);

        viewHolder.title.setText(ticket.getTitle());
        viewHolder.body.setText(ticket.getBody());
        viewHolder.price.setText(ticket.getPrice());
        return convertView;
    }
}
