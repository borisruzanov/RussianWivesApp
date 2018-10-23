package com.borisruzanov.russianwives.mvp.ui.chatmessage

import android.net.Uri

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.mvp.model.interactor.chatmessage.ChatMessageInteractor
import com.borisruzanov.russianwives.utils.UpdateCallback
import javax.inject.Inject

@InjectViewState
class ChatMessagePresenter @Inject constructor(private val interactor: ChatMessageInteractor)
    : MvpPresenter<ChatMessageView>() {

    fun initChat(friendUid: String) {
        interactor.initChat(friendUid)
    }

    fun sendMessage(friendUid: String, message: String) {
        interactor.sendMessage(friendUid, message, UpdateCallback{viewState.setEmptyMessage() })
    }

    fun sendImage(friendUid: String, imageUri: Uri) {
        interactor.sendImage(friendUid, imageUri, UpdateCallback { viewState.setEmptyMessage() })
    }

}
