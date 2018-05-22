package com.box8.login.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.box8.login.R;
import com.box8.login.base.BaseResponse;
import com.box8.login.interfaces.OtpInterface;

public class OtpVerificationFragment extends android.app.Fragment implements OtpInterface {
    public Context mContext;
    public OtpVerificationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mContext == null ) {
            mContext = this.getActivity().getApplicationContext();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContext == null ) {
            mContext = this.getActivity().getApplicationContext();
        }
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.frag_otpverification, null);
    }

    @Override
    public void PostOtpVerification(BaseResponse response) {
        if(mContext != null) {
            Toast.makeText(mContext, "OTPVerificationFragment in PostOtpVerification called", Toast.LENGTH_SHORT).show();
            System.out.println("OTPVerificationFragment in PostOtpVerification called");
        }
    }
}
