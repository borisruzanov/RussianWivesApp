package com.borisruzanov.russianwives.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.models.Users;
import com.borisruzanov.russianwives.mvp.model.interactor.SearchInteractor;
import com.borisruzanov.russianwives.mvp.view.SearchView;
import com.borisruzanov.russianwives.utils.UsersListCallback;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Михаил on 03.05.2018.
 */

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {

    private SearchInteractor searchInteractor;

    public SearchPresenter(SearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        List<SearchModel> searchModels = Arrays.asList(new SearchModel("name", "John"),
                new SearchModel("hobby", "adventures"));
        searchInteractor.searchByListParams(searchModels, new UsersListCallback() {
            @Override
            public void getUsers(List<Users> usersList) {
                if(usersList.isEmpty()) Log.d("Search", "Search list is empty");

                for (Users model: usersList) {
                    Log.d("Search", model.getName());
                }
            }
        });
    }

    public void getUsers(){
        searchInteractor.getUsers(new UsersListCallback() {
            @Override
            public void getUsers(List<Users> usersList) {
                if(!usersList.isEmpty()) getViewState().showUsers(usersList);
                else getViewState().showEmpty(true);
            }
        });
    }

}
