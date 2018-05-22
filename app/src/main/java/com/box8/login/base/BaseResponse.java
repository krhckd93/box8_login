package com.box8.login.base;

public class BaseResponse {
    private Object result;
    private String error;

    public Object getResult() { return result; }

    public void setResult(Object result) { this.result = result; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }
}
