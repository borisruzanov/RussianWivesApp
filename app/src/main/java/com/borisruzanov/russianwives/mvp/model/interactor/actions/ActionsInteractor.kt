package com.borisruzanov.russianwives.mvp.model.interactor.actions

import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.ActionItemCallback
import javax.inject.Inject

class ActionsInteractor @Inject constructor(private val repository: UserRepository) {

    fun getActions(callback: ActionItemCallback) {
        repository.getTransformedActions(callback)
    }

}
