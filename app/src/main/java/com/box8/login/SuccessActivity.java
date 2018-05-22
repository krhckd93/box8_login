package com.box8.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.box8.login.base.BaseActivity;
import com.box8.login.helpers.SessionHelper;

public class SuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Button logout = (Button)findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Implement session management
                // TODO : Logout -- Clear Token
                // Start main activity and clear activity history
                new SessionHelper(getApplicationContext()).logoutUser(SuccessActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(this, "OnBackpress is disabled", Toast.LENGTH_SHORT).show();
    }
}
