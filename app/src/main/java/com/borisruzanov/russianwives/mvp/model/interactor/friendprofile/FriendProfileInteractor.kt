package com.borisruzanov.russianwives.mvp.model.interactor.friendprofile

import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import com.borisruzanov.russianwives.utils.UserCallback
import javax.inject.Inject

class FriendProfileInteractor @Inject constructor(private val repository: FriendRepository,
                                                  private val userRepository: UserRepository,
                                                  private val ratingRepository: RatingRepository) {

    fun getFriendData(friendUid: String, userCallback: UserCallback) {
        repository.getFriendData(friendUid, userCallback)
    }

    fun setFriendVisited(friendUid: String) = repository.setUserVisited(friendUid)

    fun setFriendLiked(friendUid: String) = repository.setFriendLiked(friendUid)

    fun isUserExist(): Boolean = userRepository.isUserExist

    fun saveUser() = userRepository.saveUser()

    fun getDefaultList(callback: StringsCallback) = userRepository.getDefaultList(callback)

    fun isLiked(friendUid: String, callback: BoolCallback) = repository.isLiked(friendUid, callback)

    fun checkFullProfileAchieve(callback: BoolCallback) = ratingRepository.isAchievementExist(Achievements.FULL_PROFILE_ACH, callback)

}
