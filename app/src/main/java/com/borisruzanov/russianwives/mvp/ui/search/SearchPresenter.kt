package com.borisruzanov.russianwives.mvp.ui.search

import android.os.Bundle
import android.util.Log

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.models.Contract
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.mvp.model.interactor.search.SearchInteractor
import com.borisruzanov.russianwives.utils.UsersListCallback

import java.util.ArrayList
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(private val searchInteractor: SearchInteractor) : MvpPresenter<SearchView>() {
    private val fsUsers = ArrayList<FsUser>()

    fun getFilteredList() {
        searchInteractor.getFilteredList(UsersListCallback { userList ->
            for (fsUser in userList) {
                Log.d("LifecycleDebug", "User name is " + fsUser.name)
            }
            Log.d("LifecycleDebug", "In SearchPresenter and userList emptyness is " + userList.isEmpty())
            if (!userList.isEmpty() && fsUsers.isEmpty()) {
                fsUsers.addAll(userList)
                for (fsUser in fsUsers) {
                    Log.d("LikeDebug", "User uid is " + fsUser.uid + " name is " + fsUser.name)
                }
                Log.d("LifecycleDebug", "In SearchPresenter and fsUser empty is " + fsUsers.isEmpty())
                viewState.showUsers(fsUsers)
            }
        })
    }

    fun setFriendLiked(position: Int) {
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

        Log.d(Contract.SEARCH, "Friends name is " + fsUsers[position].uid)
        searchInteractor.setFriendLiked(fsUsers[position].uid)
    }


    fun openFriend(position: Int, args: Bundle) {
        viewState.openFriend(fsUsers[position].uid, fsUsers[position].name, args)
    }

    fun openChat(position: Int) {
        viewState.openChat(fsUsers[position].uid, fsUsers[position].name, fsUsers[position].image)
    }

}
