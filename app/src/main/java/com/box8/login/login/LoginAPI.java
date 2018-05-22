package com.box8.login.login;

import com.box8.login.base.BaseResponse;
import com.box8.login.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface LoginAPI {


    // TODO: 18-May-18 Add mail to the url
    @POST("login/")
    Call<BaseResponse> LoginWithMail(@Body User user);

    @POST("login/phone")
    Call<BaseResponse> LoginWithOtp(@Body String phone);

    @POST("login/phone/verification")
    Call<BaseResponse> VerifyOtp(@Body String phone, @Body String password);

    @DELETE("/logout")
    Call<BaseResponse> Logout(@Body String token);

}
