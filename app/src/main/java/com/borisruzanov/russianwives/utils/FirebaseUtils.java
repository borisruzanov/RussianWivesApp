package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.FsUser;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
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
        return FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken();
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

}
