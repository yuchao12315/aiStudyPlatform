package com.csuft.studyplatform.model.info;


import com.csuft.studyplatform.model.domain.IJsonModel;

import java.io.Serializable;

public class VideoModel implements Serializable, IJsonModel {
    public int picid;       //缩略图id
    public int videoid;     //视频id
    public String videoUrl; //视频地址

    @Override
    public String toString() {
        return "VideoModel{" +
                "picid=" + picid +
                ", videoid=" + videoid +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
