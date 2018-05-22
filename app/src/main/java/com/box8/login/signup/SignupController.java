package com.box8.login.signup;

import com.box8.login.base.BaseResponse;
import com.box8.login.base.RetroCustomCallback;
import com.box8.login.helpers.RetroHelper;
import com.box8.login.interfaces.SignupInterface;
import com.box8.login.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupController {

    public void Signup(final SignupInterface signupInterface, User user) {
        SignupAPI signupAPI = RetroHelper.getClient().create(SignupAPI.class);
        Call<BaseResponse> call = signupAPI.Signup(user);
        Callback<BaseResponse> callback = new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                BaseResponse baseResponse = response.body();
                signupInterface.PostSignup(baseResponse);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setError(t.toString());
                signupInterface.PostSignup(baseResponse);
                t.printStackTrace();
            }
        };
        call.enqueue(new RetroCustomCallback(callback));

    }
}
