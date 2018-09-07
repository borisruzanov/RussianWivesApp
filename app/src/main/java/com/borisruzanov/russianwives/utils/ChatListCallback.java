package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.Chat;

import java.util.List;

public interface ChatListCallback {

    void getChats(List<Chat> chatList);

}
