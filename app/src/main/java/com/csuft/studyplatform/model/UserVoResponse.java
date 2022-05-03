package com.csuft.studyplatform.model;

import java.util.List;

public class UserVoResponse {

    @Override
    public String toString() {
        return "UserVoResponse{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : false
     * code : 40000
     * msg : 账号未登录
     * data : 40001
     */
    private boolean success;
    private int code;
    private String msg;
    private UserVo data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserVo getData() {
        return data;
    }

    public void setData(UserVo data) {
        this.data = data;
    }

    public static class UserVo {

        @Override
        public String toString() {
            return "UserVo{" +
                    "id='" + id + '\'' +
                    ", salt='" + salt + '\'' +
                    ", status='" + status + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", password='" + password + '\'' +
                    ", userName='" + userName + '\'' +
                    '}';
        }

        private String id;
        private String salt;
        private String status;
        private String avatar;
        private String password;
        private String userName;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }



}