package com.borisruzanov.russianwives.mvp.model.repository;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.utils.FirebaseRequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseRepository implements IFirebaseRepository {

    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
    private StorageReference filePath = mImageStorage.child("profile_images").child("profile_image.jpg");

    private String mCurrentUser = firebaseAuth.getCurrentUser().getUid();
    private boolean isUserNotNull;
    private String deviceToken = FirebaseInstanceId.getInstance().getToken();
    private boolean needInfo = true;
    private String defaultValue = "default";


    private String getDisplayName() {
        if (currentUser != null) return currentUser.getDisplayName();
        else return "";
    }

    private String getUid() {
        if (currentUser != null) return currentUser.getUid();
        else return "";
    }

    @Override
    public boolean checkingForFirstNeededInformationOfUser() {
        mUserDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(getUid());

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                needInfo = dataSnapshot.child("drink_status").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("ethnicity").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("faith").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("gender").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("hobby").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("how_tall").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("image").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("languages").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("number_of_kids").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("relationship_status").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("smoking_status").getValue().toString().trim().toLowerCase().equals("default") ||
                        dataSnapshot.child("want_children_or_not").getValue().toString().trim().toLowerCase().equals("default");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return needInfo;
    }

    @Override
    public boolean checkForUserExist() {
        isUserNotNull = firebaseAuth.getCurrentUser() != null;
        return isUserNotNull;
    }

    @Override
    public void saveUser() {
        Log.v("Boris", "Checking from repository on user exist and saving info");
        //displayName = currentUser.getDisplayName();
        //uid = currentUser.getUid();
        mUserDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(getUid());
        mUserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("hobby").exists()) {
                    Log.v("Boris", "Adding default values in table");
                    //if (displayName.equals(""))
                    mUserDatabaseReference.setValue(FirebaseRequestManager.createNewUser(getDisplayName(), deviceToken));
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void updateBodyTypeUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("body_type").setValue(info);
    }

    @Override
    public void updateEthnicityTypeUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("ethnicity").setValue(info);
    }

    @Override
    public void updateHobbyUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("hobby").setValue(info);
    }

    @Override
    public void updateFaithUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("faith").setValue(info);
    }

    @Override
    public void updateDrinkingUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("drink_status").setValue(info);
    }

    @Override
    public void updateSmokingUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("smoking_status").setValue(info);
    }

    @Override
    public void updateRelationshipUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("relationship_status").setValue(info);
    }

    @Override
    public void updateHaveKidsUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("number_of_kids").setValue(info);
    }

    @Override
    public void updateWantKidsUserInfo(String info) {
        mRootReference.child("Users").child(mCurrentUser).child("want_children_or_not").setValue(info);
    }

    @Override
    public void insertImageInStorage(Uri resultUri) {
        filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){

                } else {

                }
            }
        });
    }
}
