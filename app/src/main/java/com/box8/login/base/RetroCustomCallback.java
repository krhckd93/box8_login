package com.box8.login.base;

import android.content.Context;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroCustomCallback implements Callback {

    private Context mContext;
    private final Callback<BaseResponse> mCallback;

    RetroCustomCallback(Context context, Callback<BaseResponse> callback) {
        this.mCallback = callback;
        this.mContext = context;
    }

    public RetroCustomCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if(mCallback != null) {
            mCallback.onResponse(call, response);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Log.e("RetroCustomCallback", t.toString());
        if(mCallback != null) {
            mCallback.onFailure(call, t);
        }
    }
}
