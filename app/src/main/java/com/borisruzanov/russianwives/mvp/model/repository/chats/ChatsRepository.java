package com.borisruzanov.russianwives.mvp.model.repository.chats;

import android.util.Log;

import com.borisruzanov.russianwives.models.Chat;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.Message;
import com.borisruzanov.russianwives.models.RtUser;
import com.borisruzanov.russianwives.models.UserChat;
import com.borisruzanov.russianwives.utils.ChatAndUidCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.RtUsersAndMessagesCallback;
import com.borisruzanov.russianwives.utils.UserChatListCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getNeededUsers;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class ChatsRepository {

    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();
    private CollectionReference users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);

    private void getChatAndUidList(ChatAndUidCallback callback) {
        DatabaseReference mConvDatabase = realtimeReference.child("Chat").child(getUid());
        mConvDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Chat> chatList = new ArrayList<>();
                        List<String> uidList = new ArrayList<>();

                        //Inflate UID List
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //This is list of chats UID's
                            uidList.add(snapshot.getKey());
                            long timeStamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());
                            boolean seen = Boolean.getBoolean(snapshot.child("seen").toString());
                            //This is list of Chats Objects
                            chatList.add(new Chat(timeStamp, seen));
                        }

                        Log.d(Contract.CHAT_LIST, "ChatList size in getChatAndUidList is " + chatList.size());
                        Log.d(Contract.CHAT_LIST, "UidList size in getChatAndUidList is " + uidList.size());
                        callback.setChatsAndUid(chatList, uidList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(Contract.CHAT_LIST, "getChatAndUidList()" + databaseError.getMessage());
                    }
                }
        );
    }

    private void getRtUsersAndMessages(List<String> uidList, RtUsersAndMessagesCallback callback) {
        List<RtUser> rtUsers = new ArrayList<>();
        List<Message> messages = new ArrayList<>();

        for (String uid : uidList) {
            realtimeReference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String online = dataSnapshot.child("online").getValue().toString();
                    String online = "true";
                    rtUsers.add(new RtUser(online));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            realtimeReference.child("Messages").child(getUid()).child(uid).limitToLast(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String message;
                            Long time;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                time = Long.valueOf(snapshot.child("time").getValue().toString());
                                message = snapshot.child("message").getValue().toString();
                                Log.d(Contract.CHAT_FRAGMENT, "dataSnapshot ащк messageList time - " +
                                        time + " message is - " + message);
                                messages.add(new Message(message, time));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(Contract.CHAT_LIST, "getRtUsersAndMessages()" + databaseError.getMessage());
                        }
                    });
            Log.d(Contract.CHAT_LIST, "RtUserList size in getUsersAndMessages is " + rtUsers.size());
            Log.d(Contract.CHAT_LIST, "MsgList size in getUsersAndMessages is " + messages.size());
            callback.setRtUsersAndMessages(rtUsers, messages);
        }
    }

    public void createUserChats(UserChatListCallback userChatListCallback) {
        final List<UserChat> userChatList = new ArrayList<>();

        getChatAndUidList((chatList, uidList) -> {

            Log.d(Contract.CHAT_LIST, "UidList size in getChatAndUidList block is " + uidList.size());
            Log.d(Contract.CHAT_LIST, "ChatList size in getChatAndUidList block is " + chatList.size());

            getRtUsersAndMessages(uidList, (rtUserList, messageList) -> {

                getNeededUsers(users, uidList, userList -> {
                    Log.d(Contract.CHAT_LIST, "UidList size in getNeededUsers block is " + uidList.size());
                    Log.d(Contract.CHAT_LIST, "ChatList size in getNeededUsers block is " + chatList.size());
                    Log.d(Contract.CHAT_LIST, "RtUserList size in getNeededUsers block is " + rtUserList.size());
                    Log.d(Contract.CHAT_LIST, "MsgList size in getNeededUsers block is " + messageList.size());
                    Log.d(Contract.CHAT_LIST, "UserList size in getNeededUsers block is " + userList.size());

                    if (!userList.isEmpty() && userChatList.isEmpty()) {
                        for (int i = 0; i < messageList.size(); i++) {
                            String name = userList.get(i).getName();
                            String image = userList.get(i).getImage();
                            String userId = userList.get(i).getUid();

                            long timeStamp = chatList.get(i).getTimeStamp();
                            boolean seen = chatList.get(i).getSeen();

                            String online = rtUserList.get(i).getOnline();

//                            String message = "test message";
                            String message = messageList.get(i).getMessage();
//                            long messageTimestamp = 235235234234L;
                            long messageTimestamp = messageList.get(i).getTime();

                            Log.d(Contract.CHAT_LIST, "User name is " + name);

                            userChatList.add(new UserChat(name, image, timeStamp, seen, userId, online,
                                    message, messageTimestamp));

                        }
                        userChatListCallback.setUserChatList(userChatList);
                    }
                });
            });

        });
    }


}
