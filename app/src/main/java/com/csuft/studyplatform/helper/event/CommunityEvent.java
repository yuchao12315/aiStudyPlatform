package com.csuft.studyplatform.helper.event;


import com.csuft.studyplatform.model.info.InfoModel;

public class CommunityEvent {
    private InfoModel mInfoModel;

    public CommunityEvent(InfoModel infoModel) {
        mInfoModel = infoModel;
    }
    public CommunityEvent(){}

    public InfoModel getInfoModel() {
        return mInfoModel;
    }

    public void setInfoModel(InfoModel infoModel) {
        mInfoModel = infoModel;
    }
}
