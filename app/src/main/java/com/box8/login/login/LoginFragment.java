package com.box8.login.login;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.box8.login.MainActivity;
import com.box8.login.R;
import com.box8.login.base.BaseResponse;
import com.box8.login.helpers.NetworkHelper;
import com.box8.login.helpers.SessionHelper;
import com.box8.login.helpers.ValidationHelper;
import com.box8.login.interfaces.LoginInterface;
import com.box8.login.models.User;
import com.google.gson.internal.LinkedTreeMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginFragment extends Fragment implements LoginInterface {
    EditText phone_email;
    EditText password;
    LoginFragment _this;
    CircularProgressButton login_btn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _this = this;
        View view = inflater.inflate(R.layout.frag_login, null);

        login_btn = (CircularProgressButton )view.findViewById(R.id.cbtn_login);
        phone_email = (EditText)view.findViewById(R.id.li_phone_email);
        password = (EditText)view.findViewById(R.id.li_password);

        phone_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!ValidationHelper.isValidMobile(v.getText().toString()) && !ValidationHelper.isValidEmail(v.getText().toString())) { setPhoneEmailError(); } else { clearPhoneEmailError(); }
                return false;
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!ValidationHelper.isValidPassword(v.getText().toString())) { setPasswordError(); } else { clearPasswordError(); }
                return false;
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkHelper.isConnected(getContext())) {
                    MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                            .content("Active internet connection is required.")
                            .title("Failed to connect!")
                            .titleGravity(GravityEnum.CENTER)
                            .neutralText("Dismiss")
                            .buttonsGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER)
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    login_btn.revertAnimation();
                                    login_btn.setInitialCornerRadius(50);
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    String str_phone_email = phone_email.getText().toString();
                    String str_password = password.getText().toString();
                    boolean password_valid = false;

                    if (ValidationHelper.isValidPassword(str_password)) {
                        password_valid = true;
                    }

                    if (ValidationHelper.isValidMobile(str_phone_email)) {
                        if (!MainActivity.OtpEnabled) {
                            if (password_valid) {
                                User user = new User();
                                user.setPhone(str_phone_email /*"admin1"*/);
                                user.setPassword(str_password/*"admin"*/);

                                new LoginController().LoginWithEmail(_this, user);
                                login_btn.startAnimation();
                            } else {
                                setPasswordError();
                            }
                        } else {
                            // TODO: OTP Verification
                        }
                    } else if (ValidationHelper.isValidEmail(str_phone_email)) {
                        if (password_valid) {
                            new LoginController().LoginWithEmail(_this, str_phone_email, str_password);
                            login_btn.startAnimation();
                        } else {
                            setPasswordError();
                        }
                    } else {
                        setPhoneEmailError();
                    }
                }
            }
        });

        InputMethodManager inputMethodManager =
                (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                view.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        return view;
    }

    @Override
    public void onDestroy() {
        login_btn.dispose();
        super.onDestroy();
    }

    void setPhoneEmailError() {
        if(phone_email != null) {
            phone_email.setError("Invalid phone or email");
        }
    }

    void clearPhoneEmailError() {
        if(phone_email != null) {
            phone_email.setError(null);
        }
    }

    void setPasswordError() {
        if(password != null) {
            password.setError("Password must be atleast 8 characters long.");
        }
    }

    void clearPasswordError() {
        if(password != null) {
            password.setError(null);
        }
    }

    @Override
    public void PostEmailLogin(final BaseResponse response) {
        if(response.getError() != null) {
            login_btn.doneLoadingAnimation(Color.parseColor("#990000"), BitmapFactory.decodeResource(getResources(), R.drawable.cross_mark_128));
            String error = response.getError();
            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .content(error.substring(0, 50>error.length() ? error.length():50  ).concat("..."))
                    .title("Login Failed!")
                    .titleGravity(GravityEnum.CENTER)
                    .neutralText("Dismiss")
                    .buttonsGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER)
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            login_btn.revertAnimation();
                            login_btn.setInitialCornerRadius(50);
                            dialog.dismiss();
                        }
                    }).show();

        } else if(response.getResult() != null) {
            login_btn.doneLoadingAnimation(Color.parseColor("#00FFFFFF"), BitmapFactory.decodeResource(getResources(), R.drawable.check_mark_white128));
            Runnable task = new Runnable() {
                public void run() {
                    new SessionHelper(getContext()).createLoginSession((String)((LinkedTreeMap) response.getResult()).get("token"));
                    ((MainActivity)getActivity()).startHomeActivity();
                }
            };
            new Handler().postDelayed(task, 2000);

        } else {
            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .content("This is weird. Let's try again!")
                    .title("Error!")
                    .titleGravity(GravityEnum.CENTER)
                    .titleColorRes(R.color.colorPrimary)
                    .neutralText("Dismiss")
                    .buttonsGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER)
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            login_btn.revertAnimation();
                            login_btn.setInitialCornerRadius(50);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void PostOtpLogin(BaseResponse response) {
        Toast.makeText(getActivity().getApplicationContext(), "PostOtpLogin called", Toast.LENGTH_SHORT).show();
        login_btn.stopAnimation();
        if(response.getError() != null) {

        } else if(response.getResult() != null) {

        } else {

        }
    }
}
