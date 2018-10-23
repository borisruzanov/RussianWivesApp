package com.borisruzanov.russianwives.mvp.ui.actions

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.models.ActionItem
import com.borisruzanov.russianwives.mvp.model.interactor.actions.ActionsInteractor
import com.borisruzanov.russianwives.mvp.ui.actions.ActionsView
import com.borisruzanov.russianwives.utils.ActionItemCallback

import java.util.ArrayList
import javax.inject.Inject

@InjectViewState
class ActionsPresenter @Inject constructor(private val interactor: ActionsInteractor) : MvpPresenter<ActionsView>() {
    private val actionItems = ArrayList<ActionItem>()

    fun setActionsList() {
        interactor.getActions(callback = ActionItemCallback { actionList ->
            if (!actionList.isEmpty() && actionItems.isEmpty()) {
                actionItems.addAll(actionList)
                viewState.showUserActions(actionItems)
            }
        })

    }

    fun openFriendProfile(position: Int) {
        viewState.openFriendProfile(actionItems[position].uid)
    }

}