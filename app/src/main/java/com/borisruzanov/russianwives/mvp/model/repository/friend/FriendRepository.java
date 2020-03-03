package com.borisruzanov.russianwives.mvp.model.repository.friend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.models.ActionModel;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.StringsCallback;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class FriendRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();
    private Prefs mPrefs;
    List<ActionModel> actionModels = new ArrayList<>();

    public FriendRepository(Prefs mPrefs) {
        this.mPrefs = mPrefs;
    }

    public void getFriendData(String docName, final UserCallback userCallback) {
        db.collection(Consts.USERS_DB).document(docName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Log.d("check", "exist");
                    userCallback.setUser(snapshot.toObject(FsUser.class));
                }
            }
        });
    }

    /**
     * Update number of visits of the friend
     * @param friendUid
     */
    public void setUserVisited(String friendUid) {
        //Adding note in Visits table
        Map<String, Object> visitAddMap = new HashMap<>();
        visitAddMap.put(Consts.TIMESTAMP, ServerValue.TIMESTAMP);
        visitAddMap.put("fromUid", getUid());
        realtimeReference.child("Visits").child(friendUid).push().updateChildren(visitAddMap);

        //Removing the old note
        removeActionItem(friendUid);

        //Updating User realtime number of visits
        realtimeReference.child("Users").child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("visits").exists()){
                    int usersVisits = Integer.valueOf(dataSnapshot.child("visits").getValue().toString());
                    int result = usersVisits + 1;
                    realtimeReference.child("Users").child(friendUid).child("visits").setValue(String.valueOf(result));
                } else {
                    realtimeReference.child("Users").child(friendUid).child("visits").setValue(String.valueOf(1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Removing old action or visit item to make db more compact
     * @param friendUid
     */
    private void removeActionItem(String friendUid) {
        realtimeReference.child("Likes").child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    actionModels.add(new ActionModel(
                            Long.valueOf(snapshot.child("timestamp").getValue().toString()),
                            snapshot.getKey(),"like"));
                }
                realtimeReference.child("Visits").child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            actionModels.add(new ActionModel(
                                    Long.valueOf(snapshot.child("timestamp").getValue().toString()),
                                    snapshot.getKey(),
                                    "visit"));
                        }
                        removeItemFromActions(friendUid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("ActionList", "getLikes" + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ActionList", "getLikes" + databaseError.getMessage());
            }
        });
    }

    /**
     * Removing note from Visits or Likes table
     * @param friendUid
     */
    private void removeItemFromActions(String friendUid){
        Collections.sort(actionModels, (e1, e2) -> Long.compare(e2.getTimestamp(), e1.getTimestamp()));
        Collections.reverse(actionModels);
        if (actionModels.size() > Integer.valueOf(mPrefs.getValue(Consts.ACTIONS_LENGTH))) {
            if (actionModels.get(0).getUid() != null && actionModels.get(0).getType() != null) {
                if (actionModels.get(0).getType().equals("like")) {
                    realtimeReference.child("Likes/").child(friendUid).child(actionModels.get(0).getUid()).removeValue();
                } else {
                    realtimeReference.child("Visits/").child(friendUid).child(actionModels.get(0).getUid()).removeValue();
                }
            }
        }
    }

    /**
     * Update realtime user likes field and add/remove note from Likes table
     * @param friendUid
     */
    public void setFriendLiked(String friendUid) {

        //Updating number of likes on realtime user field
        realtimeReference.child("Users").child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("likes").exists()){
                    int usersVisits = Integer.valueOf(dataSnapshot.child("likes").getValue().toString());
                    int result = usersVisits + 1;
                    realtimeReference.child("Users").child(friendUid).child("likes").setValue(String.valueOf(result));
                } else {
                    realtimeReference.child("Users").child(friendUid).child("likes").setValue(String.valueOf(1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Map Likes -> FrUid -> uid
        DatabaseReference userLikePush = realtimeReference.child("Likes").child(friendUid).child(getUid());

        //Map Liked -> uid -> frUid
        DatabaseReference userLikedPush = realtimeReference.child("Liked").child(getUid()).child(friendUid);

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("timestamp", ServerValue.TIMESTAMP);

        //Push to Likes table to update list in actions fragment
        userLikePush.updateChildren(messageMap, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.d("CHAT_LOG", databaseError.getMessage());
            }
        });

        //Push to Liked table to update heart icon
        userLikedPush.updateChildren(messageMap, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.d("CHAT_LOG", databaseError.getMessage());
            }
        });

        //Removing the old note
        removeActionItem(friendUid);
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
