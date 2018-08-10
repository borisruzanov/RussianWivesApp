package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UsersListCallback;

public class SearchInteractor {

    private FirebaseRepository firebaseRepository;

    public SearchInteractor(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

//    public void searchByListParams(List<SearchModel> searchModels, UsersListCallback usersListCallback){
//        firebaseRepository.searchByListParams(searchModels, usersListCallback);
//    }
//
    public void getUsers(UsersListCallback usersListCallback){
        firebaseRepository.getUsersData(usersListCallback);
    }

}
