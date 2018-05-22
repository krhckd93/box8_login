package com.box8.login.login;

import com.box8.login.base.BaseResponse;
import com.box8.login.base.RetroCustomCallback;
import com.box8.login.helpers.RetroHelper;
import com.box8.login.interfaces.LoginInterface;
import com.box8.login.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {

    // LoginWithEmail

    public void LoginWithEmail(final LoginInterface loginInterface, String email, String password) {
        LoginAPI loginAPI = RetroHelper.getClient().create(LoginAPI.class);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        LoginWithEmail(loginInterface, user);
    }

    public void LoginWithEmail(final LoginInterface loginInterface, User user) {
        LoginAPI loginAPI = RetroHelper.getClient().create(LoginAPI.class);
        Call<BaseResponse> call = loginAPI.LoginWithMail(user);
        Callback<BaseResponse> callback = new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                loginInterface.PostEmailLogin(response.body());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setError(t.toString());
                loginInterface.PostEmailLogin(baseResponse);
                t.printStackTrace();
            }
        };
        call.enqueue(new RetroCustomCallback(callback));
    }


    // LoginWithPhone

    public void LoginWithPhone(String phone) {
        LoginAPI loginAPI = RetroHelper.getClient().create(LoginAPI.class);
        Call<BaseResponse> call = loginAPI.LoginWithOtp(phone);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    // public void VerifyOTP();
    public void VerifyOtp(String phone, String otp) {
        LoginAPI loginAPI = RetroHelper.getClient().create(LoginAPI.class);
        Call<BaseResponse> call = loginAPI.VerifyOtp(phone, otp);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    // public void Logout();
    public void Logout() {
        // Logout in Session Helper to delete the session
    }
}
