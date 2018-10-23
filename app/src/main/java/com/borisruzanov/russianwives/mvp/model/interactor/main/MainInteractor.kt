package com.borisruzanov.russianwives.mvp.model.interactor.main

import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.NecessaryInfoCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import javax.inject.Inject

class MainInteractor @Inject constructor(private val userRepository: UserRepository) {

    val isUserExist: Boolean = userRepository.isUserExist

    fun saveUser() = userRepository.saveUser()

    fun getNecessaryInfo(callback: NecessaryInfoCallback) = userRepository.getNecessaryInfo(callback)

    fun hasNecessaryInfo(callback: BoolCallback) = userRepository.hasNecessaryInfo(callback)

    fun setNecessaryInfo(gender: String, age: String) = userRepository.setNecessaryInfo(gender, age)

    fun hasAdditionalInfo(callback: StringsCallback) = userRepository.hasAdditionalInfo(callback)

}
