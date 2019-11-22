package com.borisruzanov.russianwives.eventbus;

import com.borisruzanov.russianwives.models.OnlineUser;

import java.util.List;

public class ListEvent {
    List<OnlineUser> userList;

    public ListEvent(List<OnlineUser> cardList) {
        this.userList = cardList;
    }

    public List<OnlineUser> getCardList() {
        return userList;
    }

    public void setCardList(List<OnlineUser> cardList) {
        this.userList = cardList;
    }
}
