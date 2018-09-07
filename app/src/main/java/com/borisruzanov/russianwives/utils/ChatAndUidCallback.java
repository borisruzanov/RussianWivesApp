package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.Chat;

import java.util.List;

public interface ChatAndUidCallback {

    void getChatsAndUid(List<Chat> chatList, List<String> uidList);

}
