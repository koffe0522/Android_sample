package com.test.sample.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.test.sample.config.Config;
import com.test.sample.database.Ticket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MyAsyncTask extends AsyncTaskLoader {
    private String res;
    private String title, body, price;
    private Realm realm;
    private Ticket ticket;
    private long nextId;
    private Number maxId;
    private String version;
    private SharedPreferences preferences;
    private int ticketVer;
    private int newTicketVer;

    public MyAsyncTask(@NonNull Context context) {
        super(context);
        // 本メソッドは UI スレッドで処理されます。
    }


    @Override
    public Object loadInBackground() {
        preferences = getContext().getSharedPreferences("version_data", MODE_PRIVATE);
        ticketVer = preferences.getInt("TicketVer", 0);

        Request requestVer = new Request.Builder()
                .url(Config.VERSION_CHECK_API)
                .get()
                .build();
        OkHttpClient clientver = new OkHttpClient();
        clientver.newCall(requestVer).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // MISS
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                version = response.body().string();
                try {
                    JSONObject verJson = new JSONObject(version);
                    newTicketVer = Integer.parseInt(verJson.getString("version"));

                    if (newTicketVer > ticketVer) {
                        getTicketData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;

    }

    private void getTicketData() {
        Request request = new Request.Builder()
                .url(Config.GET_TICKETS_API)
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // MISS
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                try {
                    JSONObject resJson = new JSONObject(res);
                    JSONArray ticketList = resJson.getJSONArray("ticket_list");
                    for (int i = 0; i < ticketList.length(); i++) {
                        title = ticketList.getJSONObject(i).getString("title");
                        body = ticketList.getJSONObject(i).getString("body");
                        price = ticketList.getJSONObject(i).getString("price");

                        // このスレッドのためのRealmインスタンスを取得
                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                maxId = realm.where(Ticket.class).max("id");
                                nextId = 1;
                                if (maxId != null) nextId = maxId.longValue() + 1;

                                ticket = realm.createObject(Ticket.class, nextId);
                                ticket.setTitle(title);
                                ticket.setBody(body);
                                ticket.setPrice(price);

                                Log.e("ticket", String.valueOf(ticket));
                            }
                        });
                    }
                    // データの保存
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("TicketVer", newTicketVer);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
