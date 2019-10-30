package com.borisruzanov.russianwives.mvp.model.repository.friend;

import android.util.Log;
import android.view.View;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.StringsCallback;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class FriendRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();

    public void getFriendData(String docName, final UserCallback userCallback) {
        db.collection(Consts.USERS_DB).document(docName).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                Log.d("check", "exist");
                userCallback.setUser(snapshot.toObject(FsUser.class));
            }
        });
    }

    public void setUserVisited(String friendUid) {
        Map<String, Object> visitAddMap = new HashMap<>();
        visitAddMap.put(Consts.TIMESTAMP, ServerValue.TIMESTAMP);
        visitAddMap.put("fromUid", getUid());
        realtimeReference.child("Visits").child(friendUid).push().updateChildren(visitAddMap);
    }

    public void setFriendLiked(String friendUid) {
        //Map Likes -> FrUid -> uid
        DatabaseReference userLikePush = realtimeReference.child("Likes").child(friendUid).child(getUid());

        //Map Liked -> uid -> frUid
        DatabaseReference userLikedPush = realtimeReference.child("Liked").child(getUid()).child(friendUid);

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("timestamp", ServerValue.TIMESTAMP);

        userLikePush.updateChildren(messageMap, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.d("CHAT_LOG", databaseError.getMessage());
            }
        });

        userLikedPush.updateChildren(messageMap, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.d("CHAT_LOG", databaseError.getMessage());
            }
        });
    }

    public void getLikedFriends(StringsCallback callback) {
        realtimeReference.child("Liked").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> likedList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    likedList.add(snapshot.getKey());
                }
                callback.setStrings(likedList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void isLiked(String friendUid, BoolCallback callback) {
        realtimeReference.child("Liked").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> likedList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    likedList.add(snapshot.getKey());
                }
                callback.setBool(likedList.contains(friendUid));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
