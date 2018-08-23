package com.borisruzanov.russianwives.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.UserChat;

import java.util.List;

public interface ChatsView extends MvpView {
    void openChat(String uid, String name, String image);
    void showUserChats(List<UserChat> userChats);
}
