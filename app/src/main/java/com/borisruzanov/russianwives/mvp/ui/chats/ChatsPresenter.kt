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
            var seen = true
            if (userChatList.isEmpty()) {
                Log.d(Contract.CHAT_LIST, "List is empty, bye")
            } else {
                userChats.clear()
                for (userChat in userChatList) {
                    if (!userChat.seen) {
                        seen = false
                        break
                    }
                }
                if (!seen) Log.d("RealtimeChats", "Show me a highlighted tab")
                viewState.highlightChats(seen)
                userChats.addAll(userChatList)
            }
            viewState.showUserChats(userChatList)
        })
    }

    fun openChat(position: Int) {
        if (userChats.isEmpty()) {
            Log.d(Contract.TAG, "List in ChatPresenter is empty")
        } else {
            Log.d(Contract.TAG, "List in ChatPresenter isn't empty")
            if (userChats[position].userId != null) {
                viewState.openChat(userChats[position].userId, userChats[position].name,
                        userChats[position].image)
            }
            else viewState.showErrorMessage()
        }
    }
}
