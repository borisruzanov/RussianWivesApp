package com.borisruzanov.russianwives.mvp.ui.myprofile

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.UserProfileItemsList
import com.borisruzanov.russianwives.models.UserDescriptionModel
import com.borisruzanov.russianwives.mvp.model.interactor.myprofile.MyProfileInteractor
import com.borisruzanov.russianwives.utils.ActionsCountInfoCallback
import com.borisruzanov.russianwives.utils.UserCallback

import java.util.ArrayList
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(private val interactor: MyProfileInteractor) : MvpPresenter<MyProfileView>() {
    private val userDescriptionList = ArrayList<UserDescriptionModel>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setAllCurrentUserInfo()
        setActionsCount()
    }

    private fun setAllCurrentUserInfo() {
        interactor.getAllCurrentUserInfo(UserCallback{ fsUser ->
            viewState.setUserData(fsUser.name, fsUser.age, fsUser.country, fsUser.image)
            userDescriptionList.addAll(UserProfileItemsList.initData(fsUser))
            viewState.setList(userDescriptionList)
        })
    }

    private fun setActionsCount() {
        interactor.getActionsCountInfo(
                ActionsCountInfoCallback{ visits, likes -> viewState.setActionsCount(visits, likes) })
    }

}
