package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.Message;

import java.util.List;

public interface MessageListCallback {

    void getMessages(List<Message> messageList);

}
