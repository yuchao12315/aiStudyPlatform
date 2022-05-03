package com.csuft.studyplatform.model.info;



import com.csuft.studyplatform.model.domain.IJsonModel;

import java.io.Serializable;


public class PicModel implements Serializable, IJsonModel {
    /**
     * 图片id
     */
    public int picid;
    /**
     * 图片地址
     */
    public String imageUrl;

}
