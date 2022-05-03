package com.csuft.studyplatform.helper.event;


import com.csuft.studyplatform.model.info.InfoModel;

public class HomeworkEvent {
    private InfoModel mInfoModel;

    public HomeworkEvent(InfoModel infoModel){
        this.mInfoModel = infoModel;
    }

    public HomeworkEvent(){}

    public InfoModel getInfoModel() {
        return mInfoModel;
    }

    public void setInfoModel(InfoModel infoModel) {
        mInfoModel = infoModel;
    }
}
