package com.wwp.QA.Login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    // from database
    @SerializedName("loginname")
    private String loginname;
    @SerializedName("password")
    private String password;
    @SerializedName("islogin")
    private Boolean isLogin;
    @SerializedName("timestamplogin")
    private String timestampLogin;
    @SerializedName("timestamplogout")
    private String timestampLogout;


    // constructor
    public LoginResponse(LoginResponse lr){
        this.setLoginname(lr.getLoginname());
        this.setPassword(lr.getPassword());
        this.setLogin(lr.getLogin());
        this.setTimestampLogin(lr.getTimestampLogin());
        this.setTimestampLogout(lr.getTimestampLogout());
    }


    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getTimestampLogout() {
        return timestampLogout;
    }

    public void setTimestampLogout(String timestampLogout) {
        this.timestampLogout = timestampLogout;
    }

}
