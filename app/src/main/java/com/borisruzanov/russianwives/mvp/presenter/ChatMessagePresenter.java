package com.borisruzanov.russianwives.mvp.presenter;

import android.net.Uri;

import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.ChatMessageInteractor;
import com.borisruzanov.russianwives.mvp.view.ChatMessageView;

public class ChatMessagePresenter extends MvpPresenter<ChatMessageView> {

    private ChatMessageInteractor interactor;

    public ChatMessagePresenter(ChatMessageInteractor interactor) {
        this.interactor = interactor;
    }

    public void initChat(String friendUid){
        interactor.initChat(friendUid);
    }

    public void sendMessage(String friendUid, String message){
        interactor.sendMessage(friendUid, message, () -> getViewState().setEmptyMessage());
    }

    public void sendImage(String friendUid, Uri imageUri){
        interactor.sendImage(friendUid, imageUri, () -> getViewState().setEmptyMessage());
    }

}
