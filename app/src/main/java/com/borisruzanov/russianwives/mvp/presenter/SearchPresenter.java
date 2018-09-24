package com.borisruzanov.russianwives.mvp.presenter;

import android.animation.ValueAnimator;
import android.os.Bundle;
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

    public void getFilteredList(){
        searchInteractor.getFilteredList(userList -> {
            for (FsUser fsUser : userList) {
                Log.d("LifecycleDebug", "User name is "+ fsUser.getName());
            }
            Log.d("LifecycleDebug", "In SearchPresenter and userList emptyness is " + userList.isEmpty());
            if (!userList.isEmpty() && fsUsers.isEmpty()) {
                fsUsers.addAll(userList);
                for(FsUser fsUser: fsUsers){
                    Log.d("LikeDebug", "User uid is " + fsUser.getUid() + " name is " + fsUser.getName());
                }
                Log.d("LifecycleDebug", "In SearchPresenter and fsUser empty is " + fsUsers.isEmpty());
                getViewState().showUsers(fsUsers);
            }
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


    public void openFriend(int position, Bundle args) {
        getViewState().openFriend(fsUsers.get(position).getUid(), fsUsers.get(position).getName(), args);
    }
    public void openChat(int position) {
        getViewState().openChat(fsUsers.get(position).getUid(), fsUsers.get(position).getName(), fsUsers.get(position).getImage());
    }

}
