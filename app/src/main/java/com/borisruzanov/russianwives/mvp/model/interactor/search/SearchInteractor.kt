package com.borisruzanov.russianwives.mvp.model.interactor.search

import com.borisruzanov.russianwives.mvp.model.repository.filter.FilterRepository
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository
import com.borisruzanov.russianwives.mvp.model.repository.hots.HotUsersRepository
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.FULL_PROFILE_ACH
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingManager
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.HotUsersCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import com.borisruzanov.russianwives.utils.UsersListCallback
import javax.inject.Inject

class SearchInteractor @Inject constructor(private val searchRepository: SearchRepository,
                                           private val filterRepository: FilterRepository,
                                           private val friendRepository: FriendRepository,
                                           private val ratingRepository: RatingRepository,
                                           private val userRepository: UserRepository,
                                           private val hotsRepository: HotUsersRepository) {

    fun setFriendLiked(friendUid: String) {
        friendRepository.setFriendLiked(friendUid)
    }

    fun isFriendLiked(friendUid: String, callback: BoolCallback) = friendRepository.isLiked(friendUid, callback)

    fun getFilteredUserList(usersListCallback: UsersListCallback, page: Int) {
        searchRepository.getUsers(filterRepository.filteredSearchResult, usersListCallback, page)
    }

    fun addRatingLiked(friendUid: String) = userRepository.addRating(friendUid, RatingManager.FRIEND_LIKE_POINTS)

    fun hasMustInfo(callback: BoolCallback) {
        userRepository.hasMustInfo(callback)
    }

    fun getHotUsers(callback: HotUsersCallback) = hotsRepository.getHotUsers(callback)

    fun getDefaultList(callback: StringsCallback) = userRepository.getDefaultList(callback)

    fun checkFullProfileAchieve(callback: BoolCallback) = ratingRepository.isAchievementExist(FULL_PROFILE_ACH, callback)

}
