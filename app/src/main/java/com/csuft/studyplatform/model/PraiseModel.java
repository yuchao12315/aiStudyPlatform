package com.csuft.studyplatform.model;

import com.csuft.studyplatform.model.domain.IJsonModel;

import java.io.Serializable;


public class PraiseModel implements IJsonModel,Serializable {
    public boolean isInsert;
    public int praiseCount;
}
