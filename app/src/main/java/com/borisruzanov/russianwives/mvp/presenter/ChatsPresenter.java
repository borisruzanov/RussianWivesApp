package com.borisruzanov.russianwives.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.models.Chat;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.Message;
import com.borisruzanov.russianwives.models.RtUser;
import com.borisruzanov.russianwives.models.UserChat;
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

    private List<UserChat> userChats = new ArrayList<>();

    private MessageListCallback messageListCallback;
    private RtUsersCallback rtUsersCallback;

    public ChatsPresenter() {
    }

    //TODO REFACTOR Chats Fragment with repository
    public void getUserChatList() {
        /*String mCurrent_user_id = new FirebaseRepository().getUid();
        DatabaseReference mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConvDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<RtUser> rtUsers = new ArrayList<>();
                List<Chat> chatList = new ArrayList<>();
                List<String> uidList = new ArrayList<>();
                final List<Message> messageList = new ArrayList<>();

                //Inflate UID List
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //This is list of chats UID's
                    uidList.add(snapshot.getKey());
                    long timeStamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());
                    boolean seen = Boolean.getBoolean(snapshot.child("seen").toString());
                    //This is list of Chats Objects
                    chatList.add(new Chat(timeStamp, seen));
                }

                loggingUidList(uidList);
                //loggingChatList(chatList);

                for (String uid : uidList) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String online = dataSnapshot.child("online").getValue().toString();
                                    rtUsers.add(new RtUser(online));
                                    //rtUsersCallback.getUsers(rtUsers);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}

                            });
                }

                loggingRtUsersList(rtUsers);

                for (String uidq : uidList) {
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(mCurrent_user_id)
                            .child(uidq)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String message = "message is empty";
                                    Long time = 0L;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        time = Long.valueOf(snapshot.child("time").getValue().toString());
                                        message = snapshot.child("message").getValue().toString();
                                        Log.d(Contract.CHAT_FRAGMENT, "dataSnapshot ащк messageList time - " +
                                        time + " message is - " + message);
                                        messageList.add(new Message(message, time));
                                        messageListCallback.getMessages(messageList);
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}});
                }
                if(messageList.isEmpty()){
                    Log.d(Contract.CHAT_FRAGMENT, "messageList is empty");
                } else {
                    loggingMessagesList(messageList);
                }

                new FirebaseRepository().getNeededUsers(uidList, userList -> {
                    if (!userList.isEmpty()) {
                        for (int i = 0; i < userList.size(); i++) {
                            String name = userList.get(i).getName();
                            String image = userList.get(i).getImage();
                            String userId = userList.get(i).getUid();

                            long timeStamp = chatList.get(i).getTimeStamp();
                            boolean seen = chatList.get(i).getSeen();

                            String online = rtUsers.get(i).getOnline();

                            String message = messageList.get(i).getMessage();
                            long messageTimestamp = messageList.get(i).getTime();

                            userChats.add(new UserChat(name, image, timeStamp, seen, userId, online,
                                    message, messageTimestamp));

                            loggingValuesForUserChats(name, image, userId, online, message);
                            getViewState().showUserChats(userChats);
                        }
                        loggingUserChats(userChats);
                    } else {
                        Log.d(Contract.ERROR, "ChatsPresenter - FirebaseRepository().getNeededUsers - userList.isEmpty()");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        new FirebaseRepository().createUserChats(userChatList -> {
            userChats.addAll(userChatList);
            /*for (UserChat userChat: userChats){
                Log.d(Contract.CHAT_LIST, "User name is " + userChat.getName());
            }*/
            getViewState().showUserChats(userChatList);
        });
    }

    private void loggingValuesForUserChats(String name, String image, String userId, String online, String message) {
        Log.d(Contract.CHAT_FRAGMENT, "Values of item - name is- " + name + "image is- " + image +
                " userID is- " + userId + "timestamp is- " + "online is- " + online + "message is- " + message);
    }

    private void loggingUserChats(List<UserChat> userChats) {
        for (int i = 0; i < userChats.size(); i++) {
            Log.d(Contract.CHAT_FRAGMENT, "Отображаем имя в UI фрагмента  " + userChats.get(i).getName());
            Log.d(Contract.CHAT_FRAGMENT, "Отображаем видимость в UI фрагмента  " + userChats.get(i).getSeen());
            Log.d(Contract.CHAT_FRAGMENT, "Отображаем таймстэмп чата в UI фрагмента  " + userChats.get(i).getChatTimestamp());
            Log.d(Contract.CHAT_FRAGMENT, "Отображаем таймстэмп сообщения в UI фрагмента  " + userChats.get(i).getMessageTimestamp());
            Log.d(Contract.CHAT_FRAGMENT, "Отображаем онлайн статус " + userChats.get(i).getOnline());
        }
    }

    private void loggingMessagesList(List<Message> messageList) {
        if (messageList.isEmpty()) {
            for (Message message : messageList) {
                Log.d(Contract.CHAT_FRAGMENT, "messageList item - type is- " + message.getType()
                        + "- from is- " + message.getFrom()
                        + "- message is- " + message.getMessage()
                        + "- time is- " + message.getTime());
            }
        } else Log.d(Contract.CHAT_FRAGMENT, "messageList is empty");
    }

    private void loggingRtUsersList(List<RtUser> rtUsers) {
        for (RtUser rtUser : rtUsers) {
            Log.d(Contract.CHAT_FRAGMENT, "rtUsers item - online is- " + rtUser.getOnline());
        }
    }

    private void loggingChatList(List<Chat> chatList) {
        for (Chat chat : chatList) {
            Log.d(Contract.CHAT_FRAGMENT, "chatList item - timeStamp is- " + chat.getTimeStamp() + " - seen is- " + chat.getSeen());
        }
    }

    private void loggingUidList(List<String> uidList) {
        for (String uid : uidList) {
            Log.d(Contract.CHAT_FRAGMENT, "uidList item - " + uid);
        }
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
