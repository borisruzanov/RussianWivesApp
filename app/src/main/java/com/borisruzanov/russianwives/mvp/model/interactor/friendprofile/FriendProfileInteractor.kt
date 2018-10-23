package com.borisruzanov.russianwives.mvp.model.interactor.friendprofile

import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.UserCallback
import javax.inject.Inject

class FriendProfileInteractor @Inject constructor(private val repository: FriendRepository,
                                                  private val userRepository: UserRepository) {

    fun getFriendData(friendUid: String, userCallback: UserCallback) {
        repository.getFriendData(friendUid, userCallback)
    }

    fun setFriendVisited(friendUid: String) = repository.setUserVisited(friendUid)

    fun setFriendLiked(friendUid: String) = repository.setFriendLiked(friendUid)

    fun isUserExist(): Boolean = userRepository.isUserExist

    fun saveUser() = userRepository.saveUser()

}
