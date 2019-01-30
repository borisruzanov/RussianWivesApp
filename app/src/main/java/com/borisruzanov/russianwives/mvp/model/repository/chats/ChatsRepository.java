package com.borisruzanov.russianwives.mvp.model.repository.chats;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.borisruzanov.russianwives.models.Chat;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.Message;
import com.borisruzanov.russianwives.models.RtUser;
import com.borisruzanov.russianwives.models.UserChat;
import com.borisruzanov.russianwives.models.UserRt;
import com.borisruzanov.russianwives.utils.ChatAndUidCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.RealtimeUsersCallback;
import com.borisruzanov.russianwives.utils.RtUsersAndMessagesCallback;
import com.borisruzanov.russianwives.utils.UserChatListCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getNeededUsers;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class ChatsRepository {

    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();

    private void getUsers(List<String> uidList, RealtimeUsersCallback callback){
        realtimeReference.child(Consts.USERS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserRt> userRtList = new ArrayList<>();
                for (String uid: uidList) {
                    DataSnapshot snapshot = dataSnapshot.child(uid);
                    userRtList.add(new UserRt(snapshot.child(Consts.UID).getValue(String.class),
                            snapshot.child(Consts.NAME).getValue(String.class),
                            snapshot.child(Consts.IMAGE).getValue(String.class),
                            snapshot.child("online").getValue().toString(),
                            snapshot.child(Consts.DEVICE_TOKEN).getValue(String.class)));
                }
                if (userRtList.isEmpty())Log.d("RealtimeDebug", "List is empty");
                callback.setUsers(userRtList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void getChats(UserChatListCallback userChatListCallback) {
        realtimeReference.child("Messages").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> uidList =  new ArrayList<>();
                List<Message> messageList = new ArrayList<>();
                List<UserChat> userChatList = new ArrayList<>();
                Map<String, UserChat> userChatMap = new HashMap<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    uidList.add(key);

                    snapshot.getRef().limitToLast(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                messageList.add(snapshot.getValue(Message.class));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }

                getUsers(uidList, userList -> {
                        for (int i = 0; i < userList.size(); i++) {
                            String name = userList.get(i).getName();
                            String image = userList.get(i).getImage();
                            String userId = userList.get(i).getUid();
                            String online = userList.get(i).getOnline();

                            boolean seen = messageList.get(i).isSeen();
                            long messageTimestamp = messageList.get(i).getTime();
                            String message = messageList.get(i).getMessage();

                            userChatMap.put(userId, new UserChat(name, image, seen, userId, online, message, messageTimestamp));
                    }
                    Log.d("RealtimeChats", "Updating now ...");
                    userChatList.addAll(userChatMap.values());\
                    userChatListCallback.setUserChatList(sortByDate(userChatList));
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private List<UserChat> sortByDate(List<UserChat> userChats) {
        //sort list by date
        Collections.sort(userChats, (t1, t2) -> {
            Date date1 = new Date(t1.getMessageTimestamp() * 1000);
            Date date2 = new Date(t2.getMessageTimestamp() * 1000);
            return date2.compareTo(date1);
        });

        return userChats;
    }

}
