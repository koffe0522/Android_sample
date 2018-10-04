package com.test.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.test.sample.firebase.MyFirebaseInstanceIDService;
import com.test.sample.fragment.CouponFragment;
import com.test.sample.fragment.MainFragment;
import com.test.sample.fragment.MenuPagerFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case com.test.ramenapp.R.id.navigation_home:
                        MainFragment mainFragment = new MainFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(com.test.ramenapp.R.id.container, mainFragment)
                                .commit();
                        return true;
                    case com.test.ramenapp.R.id.navigation_menu:
                        MenuPagerFragment menuPagerFragment = new MenuPagerFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(com.test.ramenapp.R.id.container, menuPagerFragment)
                                .commit();
                        return true;
                    case com.test.ramenapp.R.id.navigation_ticket:
                        CouponFragment couponFragment = new CouponFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(com.test.ramenapp.R.id.container, couponFragment)
                                .commit();
                        return true;
                }
                return false;
            };

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.test.ramenapp.R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        SharedPreferences preferences = getSharedPreferences("push_token_data",MODE_PRIVATE);
        boolean state = preferences.getBoolean("send", false);

        if (!state) {
            // first time only
            MyFirebaseInstanceIDService service = new MyFirebaseInstanceIDService();
            service.onTokenRefresh();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("send", true);
            editor.apply();
        }

        Bundle fireLogBundle = new Bundle();
        fireLogBundle.putString("TEST", "FireSample app MainActivity.onCreate() is called.");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, fireLogBundle);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(com.test.ramenapp.R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MainFragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(com.test.ramenapp.R.id.container, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
