package com.box8.login;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.box8.login.helpers.SessionHelper;
import com.box8.login.viewpager.LoginViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    public static boolean OtpEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!new SessionHelper(getApplicationContext()).isLoggedIn()) {
            mViewPager = (ViewPager)findViewById(R.id.viewpager);
            LoginViewPagerAdapter viewpagerAdapter = new LoginViewPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(viewpagerAdapter);
        } else {
            startHomeActivity();
        }

    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    public void startHomeActivity() {
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_left );
    }
}
