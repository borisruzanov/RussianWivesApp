package com.borisruzanov.russianwives.mvp.ui.search

import android.os.Bundle
import android.util.Log

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.mvp.model.interactor.search.SearchInteractor
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.FirebaseUtils.isUserExist
import com.borisruzanov.russianwives.utils.UsersListCallback

import java.util.ArrayList
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(private val searchInteractor: SearchInteractor) : MvpPresenter<SearchView>() {
    private val fsUsers = ArrayList<FsUser>()

    fun getUserList(page: Int) {
        Log.d("UsersListDebug", "in getUserList")
        searchInteractor.getFilteredUserList(usersListCallback, page)
    }

    private val usersListCallback = UsersListCallback { userList ->
        if (userList.isNotEmpty()) {
            fsUsers.addAll(userList)
            viewState.addUsers(fsUsers)
        }
    }

    fun setProgressBar(isLoading: Boolean) {
        viewState.setProgressBar(isLoading)
    }

    fun onUpdate() {
        fsUsers.clear()
        viewState.clearUsers()
        getUserList(0)
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
        if (isUserExist()) searchInteractor.setFriendLiked(fsUsers[position].uid)
        else viewState.showRegistrationDialog()
    }


    fun openFriend(position: Int, args: Bundle) {
        viewState.openFriend(fsUsers[position].uid, fsUsers[position].name, args)
    }

    fun openChat(position: Int) {
        if (isUserExist()){
            searchInteractor.checkFullProfileAchieve(callback = BoolCallback { hasAchieve ->
                if (hasAchieve
            ) viewState.openChat(fsUsers[position].uid, fsUsers[position].name, fsUsers[position].image)
                else viewState.showFullProfileMessage()
            })
        }
        else viewState.showRegistrationDialog()
    }

}
