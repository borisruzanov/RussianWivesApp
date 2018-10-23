package com.borisruzanov.russianwives.mvp.model.repository.friend;

import android.util.Log;

import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
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

    public void setUserVisited(String friendUid){
        Map<String, Object> visitAddMap = new HashMap<>();
        visitAddMap.put(Consts.TIMESTAMP, ServerValue.TIMESTAMP);
        visitAddMap.put("fromUid", friendUid);

        DatabaseReference activityDb = realtimeReference.child("Visits").child(getUid()).push();
        activityDb.updateChildren(visitAddMap);

        activityDb.child("Visits").child(friendUid).child(getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> visitUserMap = new HashMap<>();
                        visitUserMap.put("Visits/" + getUid() + "/" + friendUid, visitAddMap);
                        visitUserMap.put("Visits/" + friendUid + "/" + getUid(), visitAddMap);
                        activityDb.setValue(visitUserMap, (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                Log.d(Contract.TAG, "initializeChat Error is " + databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public void setFriendLiked(String friendUid){
        DatabaseReference userLikePush = realtimeReference.child("Likes")
                .child(getUid()).child(friendUid);

        //Map Likes -> uid -> FrUid

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("timestamp", ServerValue.TIMESTAMP);

        userLikePush.updateChildren(messageMap, (databaseError, databaseReference) -> {
            if (databaseError != null) {

                Log.d("CHAT_LOG", databaseError.getMessage());

            }

        });
    }

}
