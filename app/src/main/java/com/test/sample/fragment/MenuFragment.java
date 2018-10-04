package com.test.sample.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.test.ramenapp.R;
import com.test.sample.adapter.MenuAdapter;
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


@SuppressLint("ValidFragment")
public class MenuFragment extends Fragment {

    private String res;
    private int num;
    private ArrayList<HashMap<String, String>> menuArrayList;
    private GridView mGridView;

    public MenuFragment(int position) {
        this.num = position + 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        getMenuData(view);
        return view;
    }

    private void getMenuData(View view) {
        Request request = new Request.Builder()
                .url(Config.GET_MENU_API)
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // miss
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                menuArrayList = new ArrayList<>();
                try {
                    JSONObject resJson = new JSONObject(res);
                    JSONArray menuList = resJson.getJSONArray("menu_list");
                    for (int i = 0; i < menuList.length(); i++) {
                        if (menuList.getJSONObject(i).getInt("category_num") == num) {
                            HashMap<String, String> menuMap = new HashMap<>();
                            menuMap.put("name", menuList.getJSONObject(i).getString("name"));
                            menuMap.put("url", menuList.getJSONObject(i).getString("image_url"));
                            menuMap.put("price", String.valueOf(menuList.getJSONObject(i).getInt("price")));
                            menuArrayList.add(menuMap);
                        }
                    }

                    Handler mHandler = new Handler(Looper.getMainLooper());

                    mHandler.post(() -> {
                        mGridView = (GridView) view.findViewById(R.id.menu_list);
                        MenuAdapter menuAdapter = new MenuAdapter(getActivity(),menuArrayList);
                        mGridView.setAdapter(menuAdapter);
                    });

                } catch (JSONException e) {

                }
            }
        });
    }
}
