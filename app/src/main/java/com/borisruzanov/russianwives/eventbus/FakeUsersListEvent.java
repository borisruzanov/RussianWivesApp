package com.borisruzanov.russianwives.eventbus;

import com.borisruzanov.russianwives.models.OnlineUser;

import java.util.List;

public class FakeUsersListEvent {
    List<OnlineUser> userList;

    public FakeUsersListEvent(List<OnlineUser> fakeUsersList) {
        this.userList = fakeUsersList;
    }

    public List<OnlineUser> getFakeUserList() {
        return userList;
    }

    public void setFakeUserList(List<OnlineUser> cardList) {
        this.userList = cardList;
    }
}
