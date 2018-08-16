package com.borisruzanov.russianwives.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.mvp.model.interactor.SearchInteractor;
import com.borisruzanov.russianwives.mvp.view.SearchView;
import com.borisruzanov.russianwives.utils.UsersListCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Михаил on 03.05.2018.
 */

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {

    private SearchInteractor searchInteractor;
    private List<User> users = new ArrayList<>();

    public SearchPresenter(SearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
//        List<SearchModel> searchModels = Arrays.asList(new SearchModel("name", "John"),
//                new SearchModel("hobby", "adventures"));
//        searchInteractor.searchByListParams(searchModels, new UsersListCallback() {
//            @Override
//            public void getUsers(List<User> users) {
//                if(users.isEmpty()) Log.d("Search", "Search list is empty");
//
//                for (User model: users) {
//                    Log.d("Search", model.getName());
//                }
//            }
//        });
    }

    //TODO fix double recreation of the list after device rotation
    public void getUsers() {
        searchInteractor.getUsers(userList -> {
            if (!userList.isEmpty()) {
                users.addAll(userList);
                getViewState().showUsers(users);
            } else getViewState().showEmpty(true);
        });
    }

    public void getFilteredList(){
        Log.d("FilterDia", "In Presenter getFilteredList");
        searchInteractor.getFilteredList(userList -> {
            for (User user: userList) Log.d("FilterDia", "User name is " + user.getName());
            if (!userList.isEmpty()) {
                users.addAll(userList);
                getViewState().showUsers(userList);
            } else getViewState().showEmpty(true);

        });
    }


    public void openFriend(int position) {
        Log.d(Contract.TAG, "-----> position in presenter " + position);
        getViewState().openFriend(users.get(position).getUid(), users.get(position).getName(), users.get(position).getImage());
    }

}
