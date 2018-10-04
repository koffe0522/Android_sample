package com.test.sample.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.test.ramenapp.R;
import com.test.sample.adapter.TicketAdapter;
import com.test.sample.database.Ticket;

import io.realm.Realm;
import io.realm.RealmResults;

public class CouponFragment extends Fragment {
    ListView mListView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) view.findViewById(R.id.ticket_list);

        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Ticket> tickets = realm.where(Ticket.class).findAll();
        TicketAdapter adapter = new TicketAdapter(tickets);
        mListView.setAdapter(adapter);
    }
}
