package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.UserChat;

import java.util.List;

public interface UserChatListCallback {

    void getUserChatList(List<UserChat> userChats);

}
