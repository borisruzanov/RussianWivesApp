package com.borisruzanov.russianwives.mvp.model.interactor.main

import com.borisruzanov.russianwives.mvp.model.repository.hots.HotUsersRepository
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.HotUsersCallback
import com.borisruzanov.russianwives.utils.OnlineUsersCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import javax.inject.Inject

class MainInteractor @Inject constructor(private val userRepository: UserRepository,
                                         private val hotUsersRepository: HotUsersRepository) {

    fun getUserGender() = userRepository.userGender

    val isUserExist: Boolean = userRepository.isUserExist

    fun saveUser() = userRepository.saveUser()

    fun makeDialogOpenDateDefault() = userRepository.makeDialogOpenDateDefault()

    fun setFirstOpenDate() = userRepository.setFirstOpenDate()

    fun isGenderDefault() = userRepository.isGenderDefault

    fun setGender(gender: String) {
        userRepository.gender = gender
    }

    fun hasNecessaryInfo(callback: BoolCallback) = userRepository.hasNecessaryInfo(callback)

    fun hasDefaultMustInfo(callback: BoolCallback) = userRepository.hasDefaultMustInfo(callback)

    fun addMustInfo() = userRepository.addFullProfileUsers()

    fun setUserInHot() = hotUsersRepository.setUserInHot()

    fun getHotUsers(callback: HotUsersCallback) {
        hotUsersRepository.getHotUsers(callback)
    }

    fun roundRating() = userRepository.roundRating()

    fun setFPDialogLastOpenDate() = userRepository.setDialogLastOpenDate()

    fun getDefaultList(callback: StringsCallback) = userRepository.getDefaultList(callback)

}
