package com.box8.login.signup;

import com.box8.login.base.BaseResponse;
import com.box8.login.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignupAPI {
    @POST("user/create/")
    Call<BaseResponse> Signup(@Body User user);
}
