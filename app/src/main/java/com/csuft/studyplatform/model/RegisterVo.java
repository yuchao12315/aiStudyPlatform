package com.csuft.studyplatform.model;


public class RegisterVo {

    /**
     * 用户名
     */
    public String mail;
    /**
     * 用户密码
     */
    public String password;

    /**
     * 昵称
     */
    public String nickname;
    /**
     * 用户类型  1 教师  2 家长  3 管理员
     */
    public int type;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
