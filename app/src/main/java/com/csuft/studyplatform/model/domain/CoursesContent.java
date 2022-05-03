package com.csuft.studyplatform.model.domain;

import java.util.List;

public class CoursesContent {

    /**
     * success : true
     * code : 20000
     * msg : 验证码发送成功
     * data : 917531
     */

    private boolean success;
    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "{" +
                    "course_id=" + course_id +
                    ", course_name=" + course_name +
                    ", course_college=" + course_college +
                    ", course_info='" + course_info + '\'' +
                    '}';
        }

        private long course_id;
        private String course_name;
        private String course_info;
        private String course_college;

        public long getCourse_id() {
            return course_id;
        }

        public void setCourse_id(long course_id) {
            this.course_id = course_id;
        }

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getCourse_info() {
            return course_info;
        }

        public void setCourse_info(String course_info) {
            this.course_info = course_info;
        }

        public String getCourse_college() {
            return course_college;
        }

        public void setCourse_college(String course_college) {
            this.course_college = course_college;
        }
    }
}