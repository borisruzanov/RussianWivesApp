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
    private var canLoad = false

    fun getUserList(page: Int) {
        Log.d("UsersListDebug", "in getUserList")
        searchInteractor.getFilteredUserList(usersListCallback, page)
    }

    private val usersListCallback = UsersListCallback { userList ->
        if (userList.isNotEmpty()) {
            if (!fsUsers.containsAll(userList)) {
                if (fsUsers.isNotEmpty() && fsUsers.last().uid == userList.last().uid){
                    userList.remove(userList.last())
                }
                fsUsers.addAll(userList)
                Log.d("UsersListDebug", "Add users to fsUsers and fsUsers size is " + fsUsers.size)
            }
            viewState.addUsers(userList)
        }
    }

    fun setLoading(isLoading: Boolean) {
        this.canLoad = isLoading
    }

    fun setProgressBar(isLoading: Boolean) {
        viewState.setProgressBar(isLoading)
    }

    fun onUpdate() {
        canLoad = true
        fsUsers.clear()
        viewState.clearUsers()
        getUserList(0)
    }

    fun setFriendLiked(position: Int) {
        Log.d("LikedDebug", "Liked Position is $position")
        if (isUserExist()) {
            val friendModel = fsUsers[position]
            if (fsUsers.contains(friendModel)) {
                Log.d("LikeDebug", "Friend name is ${friendModel.name} and fsUsers ")
            }
            searchInteractor.isFriendLiked(fsUsers[position].uid, callback = BoolCallback { hasLiked ->
                if (!hasLiked) {
                    searchInteractor.setFriendLiked(fsUsers[position].uid)
                    Log.d("LikedDebug", "${fsUsers[position].name} was liked")
                }
                else Log.d("LikedDebug", "${fsUsers[position].name} WAS NOT liked")
            })
        } else viewState.showRegistrationDialog()
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
