package com.box8.login.signup;

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
import android.widget.Button;
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
import com.box8.login.helpers.ValidationHelper;
import com.box8.login.interfaces.SignupInterface;
import com.box8.login.models.User;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class SignupFragment extends Fragment implements SignupInterface {

    private CircularProgressButton btn_signup;
    private EditText et_name;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_signup, null);

        et_name = (EditText)view.findViewById(R.id.su_name);
        et_email = (EditText)view.findViewById(R.id.su_email);
        et_phone = (EditText)view.findViewById(R.id.su_phone);
        et_password = (EditText)view.findViewById(R.id.su_password);

        btn_signup = (CircularProgressButton)view.findViewById(R.id.btn_signup);

        et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(et_name.getText().length() == 0) { setNameError(); } else { clearNameError(); }
                return false;
            }
        });

        et_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!ValidationHelper.isValidEmail(et_email.getText().toString())) { setEmailError(); } else { clearEmailError(); }
                return false;
            }
        });

        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!ValidationHelper.isValidMobile(et_phone.getText().toString())) { setPhoneError(); } else { clearPhoneError(); }
                return false;
            }
        });
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!ValidationHelper.isValidPassword(et_password.getText().toString())) { setPasswordError(); } else { clearPasswordError(); }
                return false;
            }
        });



        btn_signup.setOnClickListener(new View.OnClickListener() {
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
                                    btn_signup.revertAnimation();
                                    btn_signup.setInitialCornerRadius(50);
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    if (et_name.getText().length() == 0) {
                        setNameError();
                    } else if (!ValidationHelper.isValidEmail(et_email.getText().toString())) {
                        setEmailError();
                    } else if (!ValidationHelper.isValidMobile(et_phone.getText().toString())) {
                        setPhoneError();
                    } else if (!ValidationHelper.isValidPassword(et_password.getText().toString())) {
                        setPasswordError();
                    } else {
                        if (!MainActivity.OtpEnabled) {
                            User user = new User();
                            user.setFirstName(et_name.getText().toString());
                            user.setLastName(et_name.getText().toString());
                            user.setEmail(et_email.getText().toString());
                            user.setPhone(et_phone.getText().toString());
                            user.setPassword(et_password.getText().toString());
                            new SignupController().Signup(SignupFragment.this, user);
                            btn_signup.startAnimation();
                        }
                    }
                }

            }
        });
        return view;
    }


    @Override
    public void onDestroy() {

        btn_signup.dispose();
        super.onDestroy();
    }

    public void setEmailError() {
        et_email.setError("Invalid Email");
    }


    public void setNameError() {
        et_name.setError("Name cannot be empty.");
    }


    public void setPhoneError() {
        et_phone.setError("Invalid Phone");
    }


    public void setPasswordError() {
        et_password.setError("Password must be atleast 8 characters long.");
    }


    public void clearEmailError() {
        et_email.setError(null);
    }


    public void clearNameError() {
        et_name.setError(null);
    }


    public void clearPhoneError() {
        et_phone.setError(null);
    }


    public void clearPasswordError() {
        et_password.setError(null);
    }

    @Override
    public void PostSignup(BaseResponse baseResponse) {
        if(baseResponse.getError() != null) {

            btn_signup.doneLoadingAnimation(Color.parseColor("#00FFFFFF"), BitmapFactory.decodeResource(getResources(), R.drawable.cross_mark_128));

            String error = baseResponse.getError();
            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .content(error.substring(0, 50>error.length() ? error.length():50  ).concat("..."))
                    .title("Signup Failed!")
                    .titleGravity(GravityEnum.CENTER)
                    .neutralText("Dismiss")
                    .buttonsGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER)
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            btn_signup.revertAnimation();
                            btn_signup.setInitialCornerRadius(50);
                            dialog.dismiss();
                        }
                    }).show();

        } else if(baseResponse.getResult() != null) {
            if(MainActivity.OtpEnabled) {

            } else {
                try {
                    btn_signup.doneLoadingAnimation(Color.parseColor("#00FFFFFF"), BitmapFactory.decodeResource(getResources(), R.drawable.check_mark_white128));
                    final Handler handler = new Handler();
                    final Runnable task = new Runnable() {
                        public void run() {
                            ((MainActivity) getActivity()).getmViewPager().setCurrentItem(0);
                            btn_signup.setInitialCornerRadius(20);
                            btn_signup.revertAnimation();
                        }
                    };
                    handler.postDelayed(task, 1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getContext(), "PostSignup: Null Viewpager", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .content("Something went wrong. Kindly try again!")
                    .title("Error!")
                    .titleGravity(GravityEnum.CENTER)
                    .titleColorRes(R.color.colorPrimary)
                    .neutralText("Dismiss")
                    .buttonsGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER)
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            btn_signup.revertAnimation();
                            btn_signup.setInitialCornerRadius(50);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
