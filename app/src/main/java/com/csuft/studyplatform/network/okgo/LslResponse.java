package com.csuft.studyplatform.network.okgo;

import java.io.Serializable;


public class LslResponse<T> implements Serializable {

    public int code;
    public String msg;
    public T data;

    /**
     * 请求编码返回正确
     */
    public final static int RESPONSE_OK = 0;


    /**
     * 请求编码返回错误
     */
    public final static int RESPONSE_ERROR = -1;

}
