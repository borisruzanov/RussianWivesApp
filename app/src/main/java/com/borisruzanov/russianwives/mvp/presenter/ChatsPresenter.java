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
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.view.ChatsView;
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

    public ChatsPresenter() {
    }

    //TODO REFACTOR Chats Fragment with repository
    public void getUserChatList() {
        String mCurrent_user_id = new FirebaseRepository().getUid();
        DatabaseReference mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConvDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<RtUser> rtUsers = new ArrayList<>();
                List<Chat> chatList = new ArrayList<>();
                List<String> uidList = new ArrayList<>();
                List<Message> messageList = new ArrayList<>();

                //Inflate UID List
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //This is list of chats UID's
                    uidList.add(snapshot.getKey());
                    long timeStamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());
                    boolean seen = Boolean.getBoolean(snapshot.child("seen").toString());
                    //This is list of Chats Objects
                    chatList.add(new Chat(timeStamp, seen));
                }

                for (String uid : uidList) {
                    Log.d("nnn", "uidList ячейка " + uid);
                }

                for (Chat chat : chatList) {
                    Log.d("nnn", "chatList ячейка -значение timeStamp-" + chat.getTimeStamp() + " -значение seen- " + chat.getSeen());
                }


                for (String uid : uidList) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String online = dataSnapshot.child("online").getValue().toString();
                                    rtUsers.add(new RtUser(online));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }

                for (RtUser rtUser : rtUsers) {
                    Log.d("nnn", "rtUsers ячейка -значение online-" + rtUser.getOnline());
                }

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
                                        messageList.add(new Message(message, time));

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }
                for (Message message : messageList) {
                    Log.d("xxx", "messageList ячейка -значение type-" + message.getType()
                            + "-значение from" + message.getFrom()
                            + "-значение message " + message.getMessage()
                            + "-значение time" + message.getTime());
                }

                new FirebaseRepository().getNeededUsers(uidList, userList -> {

                    for (int i = 0; i < userList.size(); i++) {
                        String name = userList.get(i).getName();
                        String image = userList.get(i).getImage();
                        String userId = userList.get(i).getUid();

                        long timeStamp = chatList.get(i).getTimeStamp();
                        boolean seen = chatList.get(i).getSeen();

//                        String online = "1535475307038";
                        String online = rtUsers.get(i).getOnline();

                        String message = messageList.get(i).getMessage();
                        long messageTimestamp = messageList.get(i).getTime();

                        userChats.add(new UserChat(name, image, timeStamp, seen, userId, online,
                                message, messageTimestamp));

                        Log.d("nnn", "Отображаем имя в UI фрагмента  " + userChats.get(i).getName());
                        Log.d("nnn", "Отображаем видимость в UI фрагмента  " + userChats.get(i).getSeen());
                        Log.d("nnn", "Отображаем таймстэмп чата в UI фрагмента  " + userChats.get(i).getChatTimestamp());
                        Log.d("nnn", "Отображаем таймстэмп сообщения в UI фрагмента  " + userChats.get(i).getMessageTimestamp());

                        Log.d("nnn", "Отображаем онлайн статус " + userChats.get(i).getOnline());

                        Log.d("nnn", "Данные ячейки - имя " + name + "image " + image +
                        " userID " + userId + "timestamp " + timeStamp + "seen " + seen +
                        "online " + online + "message " + message);
//


                        getViewState().showUserChats(userChats);

                    }

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
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
