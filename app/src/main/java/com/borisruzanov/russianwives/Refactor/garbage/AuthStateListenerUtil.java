package com.borisruzanov.russianwives.Refactor.garbage;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

public class AuthStateListenerUtil {

    private DatabaseReference mUserDatabaseReference;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private boolean isUserNotNull = false;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String displayName = "";
    String uid = currentUser.getUid();
    String deviceToken = FirebaseInstanceId.getInstance().getToken();

    public AuthStateListenerUtil() {
    }


    public boolean checkForAuthState() {
        if (firebaseAuth.getCurrentUser() != null) isUserNotNull = true;
        Log.v("====>", "result is " +isUserNotNull);
        return isUserNotNull;
    }

   /* public void saveUser() {
        if (currentUser != null) {
            displayName = currentUser.getDisplayName();
            mUserDatabaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("FsUser")
                    .child(uid);
            mUserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.child("name").exists()) {
                        Log.v("MyTag", "FsUser is null");
                        System.out.println(displayName);
                        mUserDatabaseReference.setValue(FirebaseRequestManager.createNewUser(displayName, deviceToken));

                    } else {

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }*/

}
