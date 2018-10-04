package com.test.sample.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.test.ramenapp.R;
import com.test.sample.adapter.InfoAdapter;
import com.test.sample.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainFragment extends Fragment {
    private Context mContext;
    private String res;
    private ArrayList<HashMap<String, String>> infoArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getInfoData(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getInfoData(View view) {
        Request request = new Request.Builder()
                .url(Config.GET_INFO_API)
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // miss
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                infoArrayList = new ArrayList<>();
                try {
                    JSONObject resJson = new JSONObject(res);
                    JSONArray infoList = resJson.getJSONArray("info_list");

                    for (int i = 0; i < infoList.length(); i++) {
                        HashMap<String, String> menuMap = new HashMap<>();
                        menuMap.put("title", infoList.getJSONObject(i).getString("title"));
                        menuMap.put("body", infoList.getJSONObject(i).getString("body"));
                        infoArrayList.add(menuMap);
                    }

                    Handler mHandler = new Handler(Looper.getMainLooper());

                    mHandler.post(() -> {
                        ListView mListView = view.findViewById(R.id.info_list);
                        InfoAdapter infoAdapter = new InfoAdapter(infoArrayList);
                        mListView.setAdapter(infoAdapter);
                    });

                } catch (JSONException e) {
                    Log.i("JSONException", String.valueOf(e));
                }
            }
        });
    }

}
