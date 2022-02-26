package com.wwp.QA.Login;

import com.google.gson.annotations.SerializedName;

public class IsLoginResponse {


    // from database
    @SerializedName("loginname")
    private String loginname;
    @SerializedName("islogin")
    private Boolean isLogin;
    @SerializedName("timestamplogin")
    private String timestampLogin;


    // constructor
    public IsLoginResponse(LoginResponse lr){
        this.setLoginname(lr.getLoginname());
        this.setLogin(lr.getLogin());
        this.setTimestampLogin(lr.getTimestampLogin());
    }


    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public String getTimestampLogin() {
        return timestampLogin;
    }

    public void setTimestampLogin(String timestampLogin) {
        this.timestampLogin = timestampLogin;
    }



}
