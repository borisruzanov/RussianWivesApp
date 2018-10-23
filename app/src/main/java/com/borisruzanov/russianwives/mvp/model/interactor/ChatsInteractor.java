package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UserChatListCallback;

public class ChatsInteractor {

  private FirebaseRepository repository;

  public ChatsInteractor(FirebaseRepository repository) {
    this.repository = repository;
  }

  public void getUsersChats(UserChatListCallback callback){
    repository.createUserChats(callback);
  }

}