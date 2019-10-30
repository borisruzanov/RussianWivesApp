package com.borisruzanov.russianwives.mvp.model.interactor.chats

import com.borisruzanov.russianwives.mvp.model.repository.chats.ChatsRepository
import com.borisruzanov.russianwives.utils.UserChatListCallback
import javax.inject.Inject

class ChatsInteractor @Inject constructor(private val repository: ChatsRepository) {

    fun getUsersChats(callback: UserChatListCallback) {
        repository.getChats(callback)
    }

}
