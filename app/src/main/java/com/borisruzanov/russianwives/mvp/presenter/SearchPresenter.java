package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.mvp.model.interactor.SearchInteractor;
import com.borisruzanov.russianwives.mvp.view.SearchView;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {

    private SearchInteractor searchInteractor;
    private List<FsUser> fsUsers = new ArrayList<>();

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
//            public void getUsers(List<FsUser> fsUsers) {
//                if(fsUsers.isEmpty()) Log.d("Search", "Search list is empty");
//
//                for (FsUser model: fsUsers) {
//                    Log.d("Search", model.getName());
//                }
//            }
//        });
    }

    //TODO fix double recreation of the list after device rotation
    public void getUsers() {
        searchInteractor.getUsers(userList -> {
            if (!userList.isEmpty()) {
                fsUsers.addAll(userList);
                getViewState().showUsers(fsUsers);
            } else getViewState().showEmpty(true);
        });
    }

    public void getFilteredList(){
        searchInteractor.getFilteredList(userList -> {
            if (!userList.isEmpty()) {
                fsUsers.addAll(userList);
                getViewState().showUsers(userList);
            } else getViewState().showEmpty(true);
        });
    }


    public void openFriend(int position) {
        getViewState().openFriend(fsUsers.get(position).getUid(), fsUsers.get(position).getName(), fsUsers.get(position).getImage());
    }
    public void openChat(int position) {
        getViewState().openChat(fsUsers.get(position).getUid(), fsUsers.get(position).getName(), fsUsers.get(position).getImage());
    }

}
