package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UserCallback;

public class FriendProfileInteractor {

    private FirebaseRepository firebaseRepository;

    public FriendProfileInteractor(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    public void getFriendData(String friendUid, final UserCallback userCallback){
        firebaseRepository.getFriendData(Consts.USERS_DB, friendUid, userCallback);
    }

    public void setUserVisited(String friendUid){
        firebaseRepository.setUserVisited(friendUid);
    }

}
