package com.csuft.studyplatform.model;


public class LoginVo {

    /**
     * 用户名
     */
    public String userName;
    /**
     * 用户密码
     */
    public String password;

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
