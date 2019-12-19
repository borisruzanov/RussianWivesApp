package com.borisruzanov.russianwives.eventbus;

import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.models.OnlineUser;

import java.util.List;

public class UserEvent {

    FsUser mUser;

    public UserEvent(FsUser mUser) {
        this.mUser = mUser;
    }

    public FsUser getmUser() {
        return mUser;
    }

    public void setmUser(FsUser mUser) {
        this.mUser = mUser;
    }
}
