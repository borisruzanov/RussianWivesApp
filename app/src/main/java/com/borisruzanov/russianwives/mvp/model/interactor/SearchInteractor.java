package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FilterRepository;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UsersListCallback;

public class SearchInteractor {

    private FirebaseRepository firebaseRepository;
    private FilterRepository filterRepository;

    public SearchInteractor(FirebaseRepository firebaseRepository, FilterRepository filterRepository) {
        this.firebaseRepository = firebaseRepository;
        this.filterRepository = filterRepository;
    }

    public void getFilteredList(UsersListCallback usersListCallback){
        firebaseRepository.searchByListParams(filterRepository.getFilteredSearchResult(), usersListCallback);
    }

    public void getUsers(UsersListCallback usersListCallback){
        firebaseRepository.getUsersData(usersListCallback);
    }

}
