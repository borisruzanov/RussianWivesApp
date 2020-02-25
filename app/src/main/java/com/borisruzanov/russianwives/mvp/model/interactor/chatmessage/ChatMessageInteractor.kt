package com.borisruzanov.russianwives.mvp.model.interactor.chatmessage

import android.net.Uri

import com.borisruzanov.russianwives.mvp.model.repository.chatmessage.ChatMessageRepository
import com.borisruzanov.russianwives.utils.UpdateCallback
import javax.inject.Inject

class ChatMessageInteractor @Inject constructor(private val repository: ChatMessageRepository) {

    fun initChat(friendUid: String) {
        repository.initChat(friendUid)
    }

    fun sendMessage(friendUid: String, message: String, callback: UpdateCallback) {
        repository.sendMessage(friendUid, message, callback)
    }

    fun sendImage(friendUid: String, imageUri: Uri, callback: UpdateCallback) {
        repository.sendImage(friendUid, imageUri, callback)
    }

    fun removeOldMessage(chatUser: String?, id: String, message: String) {
        repository.removeOldMessage(chatUser,id,message)
    }

}
