package com.borisruzanov.russianwives.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInjections {

    public static String users = "Users";

    public static FirebaseUser currentUserInstance = FirebaseAuth.getInstance().getCurrentUser();
    public static String currentUserUid = currentUserInstance.getUid();
    public static DatabaseReference currentUserUidReference = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child(currentUserUid);
    public static String currentUserUidString = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child(currentUserUid).toString();
    public static String currentUserToken = FirebaseInstanceId.getInstance().getToken();

}
