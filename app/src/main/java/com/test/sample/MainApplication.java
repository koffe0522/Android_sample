package com.test.sample;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.test.sample.http.MyAsyncTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        TypefaceProvider.registerDefaultIconSets();

        // Realm 初期化
        Realm.init(this);
        // 各種設定するなら以下のようにする
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        if (checkConnectedWifi()) {
            Log.d("TAG", "Get location by using Wifi");
            // 非同期処理
            MyAsyncTask task = new MyAsyncTask(this);
            task.loadInBackground();
        }
    }

    private boolean checkConnectedWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo Wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return Wifi.isConnected();
    }

}
