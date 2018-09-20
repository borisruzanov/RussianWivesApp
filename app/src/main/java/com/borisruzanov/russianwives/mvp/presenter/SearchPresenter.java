package com.borisruzanov.russianwives.mvp.presenter;

import android.animation.ValueAnimator;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.models.Contract;
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
//            public void setUsers(List<FsUser> fsUsers) {
//                if(fsUsers.isEmpty()) Log.d("Search", "Search list is empty");
//
//                for (FsUser model: fsUsers) {
//                    Log.d("Search", model.getName());
//                }
//            }
//        });
    }

    public void getFilteredList(){
        searchInteractor.getFilteredList(userList -> {
            for (FsUser fsUser : userList) {
                Log.d("LifecycleDebug", "User name is "+ fsUser.getName());
            }
            Log.d("LifecycleDebug", "In SearchPresenter and userList emptyness is " + userList.isEmpty());
            if (!userList.isEmpty() && fsUsers.isEmpty()) {
                fsUsers.addAll(userList);
                Log.d("LifecycleDebug", "In SearchPresenter and fsUser empty is " + fsUsers.isEmpty());
                getViewState().showUsers(fsUsers);
            }
//            else getViewState().showEmpty(true);
        });
    }

    public void setFriendLiked(int position){
//            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    animationView.setProgress((Float) valueAnimator.getAnimatedValue());
//                }
//            });
//
//            if (animationView.getProgress() == 0f) {
//                animator.start();
//            } else {
//                animationView.setProgress(0f);
//            }

        Log.d(Contract.SEARCH, "Friends name is " + fsUsers.get(position).getUid());
        searchInteractor.setFriendLiked(fsUsers.get(position).getUid());
    }


    public void openFriend(int position) {
        getViewState().openFriend(fsUsers.get(position).getUid(), fsUsers.get(position).getName(), fsUsers.get(position).getImage());
    }
    public void openChat(int position) {
        getViewState().openChat(fsUsers.get(position).getUid(), fsUsers.get(position).getName(), fsUsers.get(position).getImage());
    }

}
