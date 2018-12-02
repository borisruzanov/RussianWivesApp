package com.borisruzanov.russianwives.mvp.model.interactor.search

import com.borisruzanov.russianwives.mvp.model.repository.filter.FilterRepository
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository
import com.borisruzanov.russianwives.utils.UsersListCallback
import javax.inject.Inject

class SearchInteractor @Inject constructor(private val searchRepository: SearchRepository,
                       private val filterRepository: FilterRepository, private val friendRepository: FriendRepository) {

    fun setFriendLiked(friendUid: String) {
        friendRepository.setFriendLiked(friendUid)
    }

    fun getFilteredUserList(usersListCallback: UsersListCallback, page: Int) {
        searchRepository.getUsers(filterRepository.filteredSearchResult, usersListCallback, page)
    }

}
