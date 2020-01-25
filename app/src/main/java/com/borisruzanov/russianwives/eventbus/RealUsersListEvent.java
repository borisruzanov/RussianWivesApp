package com.borisruzanov.russianwives.eventbus;

import com.borisruzanov.russianwives.models.OnlineUser;

import java.util.List;

public class RealUsersListEvent {
    List<OnlineUser> userList;

    public RealUsersListEvent(List<OnlineUser> realUsersList) {
        this.userList = realUsersList;
    }

    public List<OnlineUser> getRealUserList() {
        return userList;
    }

    public void setRealUserList(List<OnlineUser> cardList) {
        this.userList = cardList;
    }
}
