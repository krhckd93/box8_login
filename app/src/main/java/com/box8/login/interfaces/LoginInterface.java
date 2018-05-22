package com.box8.login.interfaces;

import com.box8.login.base.BaseResponse;

public interface LoginInterface {

    void PostEmailLogin(BaseResponse response);

    void PostOtpLogin(BaseResponse response);
}
