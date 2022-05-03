package com.csuft.studyplatform.model;

public class ResponseWithoutData {
    @Override
    public String toString() {
        return "UserVoResponse{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
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
}
