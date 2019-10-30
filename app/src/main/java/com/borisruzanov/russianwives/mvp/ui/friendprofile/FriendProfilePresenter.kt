package com.borisruzanov.russianwives.mvp.ui.friendprofile

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.UserProfileItemsList
import com.borisruzanov.russianwives.models.UserDescriptionModel
import com.borisruzanov.russianwives.mvp.model.interactor.friendprofile.FriendProfileInteractor
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.FirebaseUtils.isUserExist
import com.borisruzanov.russianwives.utils.UserCallback
import java.util.*
import javax.inject.Inject

@InjectViewState
class FriendProfilePresenter @Inject constructor(private val interactor: FriendProfileInteractor) : MvpPresenter<FriendProfileView>() {
    private val userDescriptionList = ArrayList<UserDescriptionModel>()

    fun setAllInfo(friendUid: String) {
        setFriendData(friendUid)
        if (interactor.isUserExist()){
            interactor.setFriendVisited(friendUid)
            interactor.addRatingVisited(friendUid)
        }
    }

    fun setLikeHighlighted(friendUid: String) {
        if (isUserExist()) {
            interactor.isLiked(friendUid, callback = BoolCallback {isLiked -> if (isLiked) viewState.setLikeHighlighted()})
        }
    }

    fun setFriendLiked(friendUid: String) {
        if (interactor.isUserExist()) {
            interactor.isLiked(friendUid, callback = BoolCallback { isLiked ->
                if (!isLiked) {
                    interactor.setFriendLiked(friendUid)
                    viewState.setLikeHighlighted()
                    interactor.addRatingLiked(friendUid)
                }
            })
        } else {
            viewState.openRegDialog()
        }
    }

    fun saveUser() {
        interactor.saveUser()
    }

    fun openChatMessage(friendUid: String) {
        if (interactor.isUserExist()) {
            interactor.hasMustInfo(callback = BoolCallback { hasMustInfo ->
                if (hasMustInfo) {
                    interactor.getFriendData(friendUid, UserCallback { fsUser ->
                        viewState.openChatMessage(fsUser.name, fsUser.image)
                    })
                }
                else viewState.showMustInfoDialog()
            })

        } else {
            viewState.openRegDialog()
        }
    }

    private fun setFriendData(friendUid: String) {
        interactor.getFriendData(friendUid, UserCallback { fsUser ->
            viewState.setFriendData(fsUser.name, fsUser.age, fsUser.country, fsUser.image)
            userDescriptionList.addAll(UserProfileItemsList.initData(fsUser))
            viewState.setList(userDescriptionList)
        })
    }

}
