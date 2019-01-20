package com.borisruzanov.russianwives.mvp.ui.actions

import android.util.Log
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
            if (actionList.isNotEmpty() && actionItems.isEmpty()) {
                actionItems.addAll(actionList)
            }
            else Log.d("ActionList", "Actions list is empty " + actionList.isEmpty())
            viewState.showUserActions(actionItems)
        })
    }

    fun reloadList() {
        if (actionItems.isNotEmpty()) {
            actionItems.clear()
            setActionsList()
        }
    }

    fun openFriendProfile(position: Int) {
        viewState.openFriendProfile(actionItems[position].uid)
    }

}