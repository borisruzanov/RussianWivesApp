package com.borisruzanov.russianwives.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.models.Chat;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.models.Message;
import com.borisruzanov.russianwives.models.RtUser;
import com.borisruzanov.russianwives.models.UserChat;
import com.borisruzanov.russianwives.mvp.model.interactor.ChatsInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.view.ChatsView;
import com.borisruzanov.russianwives.utils.MessageListCallback;
import com.borisruzanov.russianwives.utils.RtUsersCallback;
import com.borisruzanov.russianwives.utils.UserChatListCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class ChatsPresenter extends MvpPresenter<ChatsView> {

    private ChatsInteractor interactor;
    private List<UserChat> userChats = new ArrayList<>();

    public ChatsPresenter(ChatsInteractor interactor) {
        this.interactor = interactor;
    }

    public void getUserChatList() {
        interactor.getUsersChats(userChatList -> {
            if (userChatList.isEmpty()) Log.d(Contract.CHAT_LIST, "List is empty, bye");
            else {
                for (UserChat userChat: userChatList){
                    Log.d(Contract.CHAT_LIST, "User name is" + userChat.getName());
                }
            }
            userChats.addAll(userChatList);
            getViewState().showUserChats(userChatList);
        });
    }

    public void openChat(int position) {
        if (userChats.isEmpty()) {
            Log.d(Contract.TAG, "List in ChatPresenter is empty");
        } else {
            Log.d(Contract.TAG, "List in ChatPresenter isn't empty");
            getViewState().openChat(userChats.get(position).getUserId(), userChats.get(position).getName(),
                    userChats.get(position).getImage());
        }
    }
}
