package com.borisruzanov.russianwives.mvp.ui.chats

import android.util.Log

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.models.Contract
import com.borisruzanov.russianwives.models.UserChat
import com.borisruzanov.russianwives.mvp.model.interactor.chats.ChatsInteractor
import com.borisruzanov.russianwives.mvp.model.repository.chats.ChatsRepository
import com.borisruzanov.russianwives.utils.UserChatListCallback

import java.util.ArrayList
import javax.inject.Inject

@InjectViewState
class ChatsPresenter @Inject constructor(private val interactor: ChatsInteractor) : MvpPresenter<ChatsView>() {
    private val userChats = ArrayList<UserChat>()

    fun getUserChatList() {
        interactor.getUsersChats(UserChatListCallback {userChatList ->
            if (userChatList.isEmpty()) {
                Log.d(Contract.CHAT_LIST, "List is empty, bye")
            } else {
                for (userChat in userChatList) {
                    Log.d(Contract.CHAT_LIST, "User name is " + userChat.name)
                }
                Log.d("RealtimeChats", "UserChatList size is ${userChatList.size}")
                userChats.clear()
                userChats.addAll(userChatList)
                Log.d("RealtimeChats", "UserChats size is ${userChats.size}")
            }
            viewState.showUserChats(userChats)
        })
    }

    fun openChat(position: Int) {
        if (userChats.isEmpty()) {
            Log.d(Contract.TAG, "List in ChatPresenter is empty")
        } else {
            Log.d(Contract.TAG, "List in ChatPresenter isn't empty")
            viewState.openChat(userChats[position].userId, userChats[position].name,
                    userChats[position].image)
        }
    }
}
