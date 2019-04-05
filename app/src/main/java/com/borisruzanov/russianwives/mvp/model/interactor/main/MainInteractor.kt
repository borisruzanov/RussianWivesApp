package com.borisruzanov.russianwives.mvp.model.interactor.main

import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.NecessaryInfoCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import javax.inject.Inject

class MainInteractor @Inject constructor(private val userRepository: UserRepository) {

    val isUserExist: Boolean = userRepository.isUserExist

    fun saveUser() = userRepository.saveUser()

    fun makeDialogOpenDateDefault() = userRepository.makeDialogOpenDateDefault()

    fun setFirstOpenDate () = userRepository.setFirstOpenDate()

    fun isGenderDefault() = userRepository.isGenderDefault

    fun setGender(gender: String) = userRepository.setGender(gender)

    fun hasNecessaryInfo(callback: BoolCallback) = userRepository.hasNecessaryInfo(callback)

    fun hasDefaultMustInfo(callback: BoolCallback) = userRepository.hasDefaultMustInfo(callback)

    fun setFPDialogLastOpenDate() = userRepository.setDialogLastOpenDate()

    fun getDefaultList(callback: StringsCallback) = userRepository.getDefaultList(callback)

}
