package com.borisruzanov.russianwives.utils;

import android.support.annotation.NonNull;

import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.models.UserRt;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtils {

    public static String getUid() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isUserExist() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static String getDeviceToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public static void getNeededUsers(CollectionReference users, List<String> uidList, UsersListCallback usersListCallback) {
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (String uid : uidList) {
            Task<QuerySnapshot> documentSnapshotTask = users.whereEqualTo(Consts.UID, uid).get();
            tasks.add(documentSnapshotTask);
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(list -> {
            List<FsUser> fsUsers = new ArrayList<>();
            for (Object object : list) {
                for (DocumentSnapshot snapshot : ((QuerySnapshot) object).getDocuments()) {
                    FsUser fsUser = snapshot.toObject(FsUser.class);
                    fsUsers.add(fsUser);
                }
            }
            usersListCallback.setUsers(fsUsers);
        });
    }

    public static void getUsers(List<String> uidList, RealtimeUsersCallback callback){
        FirebaseDatabase.getInstance().getReference().child(Consts.USERS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserRt> userRtList = new ArrayList<>();
                for (String uid: uidList) {
                    DataSnapshot snapshot = dataSnapshot.child(uid);
                    userRtList.add(new UserRt(snapshot.child(Consts.UID).getValue(String.class),
                            snapshot.child(Consts.NAME).getValue(String.class),
                            snapshot.child(Consts.IMAGE).getValue(String.class),
                            String.valueOf(snapshot.child("online").getValue()),
                            snapshot.child(Consts.DEVICE_TOKEN).getValue(String.class)));
                }
                callback.setUsers(userRtList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }



}
