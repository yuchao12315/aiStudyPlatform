package com.csuft.studyplatform.model.info;


import com.csuft.studyplatform.model.domain.IJsonModel;

import java.io.Serializable;

public class UserModel implements IJsonModel, Serializable {
    public String username;
    public String avatar;
    public String nickname;
}
