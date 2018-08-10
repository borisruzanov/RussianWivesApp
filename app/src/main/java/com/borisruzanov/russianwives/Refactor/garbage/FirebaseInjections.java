package com.borisruzanov.russianwives.Refactor.garbage;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInjections {

    public static String users = "User";

    public static FirebaseUser currentUserInstance = FirebaseAuth.getInstance().getCurrentUser();
    public static String currentUserUid = currentUserInstance.getUid();
    public static DatabaseReference currentUserUidReference = FirebaseDatabase.getInstance().getReference()
            .child("User")
            .child(currentUserUid);
    public static String currentUserUidString = FirebaseDatabase.getInstance().getReference()
            .child("User")
            .child(currentUserUid).toString();
    public static String currentUserToken = FirebaseInstanceId.getInstance().getToken();

}
