package com.csuft.studyplatform.model.domain;

public class VerifiCode {

    /**
     * success : true
     * code : 20000
     * msg : 验证码发送成功
     * data : 917531
     */

    private boolean success;
    private int code;
    private String msg;
    private int data;

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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
