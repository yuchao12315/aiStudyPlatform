package com.csuft.studyplatform;


import com.csuft.studyplatform.utils.UIUtil;

/**
 * 自定义异常类
 *
 */

public class VideoException extends RuntimeException {

    public VideoException(String desc) {
        UIUtil.showToast(desc);

    }
}
