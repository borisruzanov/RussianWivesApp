package com.borisruzanov.russianwives.mvp.model.interactor;

import android.net.Uri;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UpdateCallback;

public class ChatMessageInteractor {

    private FirebaseRepository repository;

    public ChatMessageInteractor(FirebaseRepository repository) {
        this.repository = repository;
    }

    public void initChat(String friendUid){
        repository.initChat(friendUid);
    }

    public void sendMessage(String friendUid, String message, UpdateCallback callback){
        repository.sendMessage(friendUid, message, callback);
    }

    public void sendImage(String friendUid, Uri imageUri, UpdateCallback callback){
        repository.sendImage(friendUid, imageUri, callback);
    }

}
