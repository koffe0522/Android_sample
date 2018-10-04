package com.test.sample.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.test.sample.config.Config;
import com.test.sample.config.FCMtoken;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private SharedPreferences preferences;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String uuidString = UUID.randomUUID().toString();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Log.d(TAG, "uuid: " + uuidString);
        if (refreshedToken != null) {
            sendRegistrationToServer(refreshedToken, uuidString);
        }
    }

    private void sendRegistrationToServer(String token, String uuid) {
        // TODO: Implement this method to send token to your app server.
        Gson gson = new Gson();
        FCMtoken fcMtoken = new FCMtoken(token);
        String json = gson.toJson(fcMtoken);


        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .build();


        Request request = new Request.Builder()
                .url(Config.POST_FCMTOKEN_API)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.e("onResponse", res);
            }
        });



    }
}
