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
import java.util.Objects;

@InjectViewState
public class ChatsPresenter extends MvpPresenter<ChatsView> {

    private List<UserChat> userChats = new ArrayList<>();

    public ChatsPresenter() {}

    //TODO REFACTOR Chats Fragment with repository
    public void getUserChatList(){
        String mCurrent_user_id = new FirebaseRepository().getUid();
        DatabaseReference mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConvDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<RtUser> rtUsers = new ArrayList<>();
                List<Chat> chatList = new ArrayList<>();
                List<String> uidList = new ArrayList<>();
                List<Message> messageList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(Contract.TAG, "ChatsFragment - chat Request - Uid" + snapshot.getKey() + " ");
                    //This is list of chats UID's
                    uidList.add(snapshot.getKey());
                    long timeStamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());
                    boolean seen = Boolean.getBoolean(snapshot.child("seen").toString());
                    Log.d(Contract.TAG, "ChatsFragment - chat Request - Object - " + "timestamp is " + timeStamp + "message seen is " + seen);
                    //This is list of Chats Objects
                    chatList.add(new Chat(timeStamp, seen));
                }


                for(String uid: uidList){
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (Objects.requireNonNull(dataSnapshot.child("online").getValue()).toString() != null) {
                                        String online = dataSnapshot.child("online").getValue().toString();
                                        Log.d("OnlineStatusDebug", "In onDataChange " + online);
                                        rtUsers.add(new RtUser(online));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });

                    FirebaseDatabase.getInstance().getReference().child("Message").child(mCurrent_user_id)
                            .child(uid)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        long time = Long.valueOf(snapshot.child("time").getValue().toString());
                                        String message = snapshot.child("message").getValue().toString();
                                        messageList.add(new Message(message, time));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }

                /*FirebaseDatabase.getInstance().getReference().child("Users")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                    String online = snapshot.child("online").getValue().toString();
                                    Log.d("OnlineStatusDebug", "In onDataChange online is "+ online);
                                    //if (online != null) {
                                        rtUsers.add(new RtUser(online));
                                    //}
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/

                new FirebaseRepository().getNeededUsers(uidList, userList -> {
                    for (FsUser fsUser : userList) {
                        Log.d(Contract.TAG, "Полученное имя из репозитория " + fsUser.getName());
                    }

                    if (userList.isEmpty()) Log.d(Contract.TAG, "LIST IS EMPTY!!!");


                    for (int i = 0; i < userList.size(); i++) {
                        String name = userList.get(i).getName();
                        String image = userList.get(i).getImage();
                        String userId = userList.get(i).getUid();
                        long timeStamp = chatList.get(i).getTimeStamp();
                        boolean seen = chatList.get(i).getSeen();
                        String online = rtUsers.get(i).getOnline();

                        String message = messageList.get(i).getMessage();

                        userChats.add(new UserChat(name, image, timeStamp, seen, userId, online, message));

                        Log.d(Contract.TAG, "Отображаем имя в UI фрагмента  " + userChats.get(i).getName());
                        Log.d(Contract.TAG, "Отображаем видимость в UI фрагмента  " + userChats.get(i).getSeen());
                        Log.d(Contract.TAG, "Отображаем таймстэмп в UI фрагмента  " + userChats.get(i).getTimestamp());

                        Log.d("OnlineStatusDebug", "Отображаем онлайн статус " + userChats.get(i).getOnline());


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
        if(userChats.isEmpty()) {
            Log.d(Contract.TAG, "List in ChatPresenter is empty");
        } else{
            Log.d(Contract.TAG, "List in ChatPresenter isn't empty");
            getViewState().openChat(userChats.get(position).getUserId(), userChats.get(position).getName(),
                    userChats.get(position).getImage());
        }
    }
}
