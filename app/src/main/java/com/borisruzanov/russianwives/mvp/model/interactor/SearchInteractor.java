package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.IsDataLoadingCallback;
import com.borisruzanov.russianwives.utils.ListLoadingHelper;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.google.firebase.firestore.Query;

import java.util.List;

/**
 * Created by Михаил on 05.05.2018.
 */

public class SearchInteractor implements ISearchInteractor {

    private FirebaseRepository firebaseRepository;

    public SearchInteractor(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    @Override
    public void getUsers(String nodeId, UsersListCallback usersListCallback, IsDataLoadingCallback isDataLoadingCallback) {
        //firebaseRepository.getUsers(nodeId, usersListCallback, isDataLoadingCallback);
    }

    public void searchByListParams(List<SearchModel> searchModels, UsersListCallback usersListCallback){
        firebaseRepository.searchByListParams(searchModels, usersListCallback);
    }

    public void getUsers(UsersListCallback usersListCallback){
        firebaseRepository.getUsersData(usersListCallback);
    }

    public Query getUsersQuery(){
        return firebaseRepository.getUsersQuery();
    }

    public void searchByName(String searchName){
        firebaseRepository.searchByName(searchName);
    }

    @Override
    public boolean getDataLoadingStatus() {
        return firebaseRepository.isDataLoading();
    }

    @Override
    public void setDataLoading(boolean isDataLoading) {
        firebaseRepository.setDataLoading(isDataLoading);
    }
}
