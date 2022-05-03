package com.csuft.studyplatform.model;

import com.csuft.studyplatform.model.domain.IJsonModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class User implements IJsonModel,Serializable{
    /**
     * 用户名
     */
    public String getUserName;
    /**
     * 用户密码
     */
    public String password;
    /**
     * 用户头像地址
     */
    @SerializedName("avatar")
    public String icon;
    /**
     * 生日
     */
    public String birthday;
    /**
     * 昵称
     */
    public String nickname;
    /**
     * 用户类型  1 教师  2 家长  3 管理员 4 游客
     */
    public int type;
    /**
     * 班级id
     */
    public int classid;
    /**
     * 家庭地址
     */
    public String address;
    /**
     * 孩子名字
     */
    @SerializedName("child_name")
    public String childName;
    /**
     * 孩子头像地址
     */
    @SerializedName("child_avatar")
    public String childAvatar;

    @Override
    public String toString() {
        return "User{" +
                "username='" + getUserName + '\'' +
                ", password='" + password + '\'' +
                ", icon='" + icon + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type=" + type +
                ", classid=" + classid +
                ", address='" + address + '\'' +
                ", childName='" + childName + '\'' +
                ", childAvatar='" + childAvatar + '\'' +
                '}';
    }
}
