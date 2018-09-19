package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.UserChat;

import java.util.List;

public interface UserChatListCallback {

    void setUserChatList(List<UserChat> userChats);

}
