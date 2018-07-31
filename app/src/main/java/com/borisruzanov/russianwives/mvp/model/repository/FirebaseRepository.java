package com.borisruzanov.russianwives.mvp.model.repository;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.models.Users;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.FirebaseRequestManager;
import com.borisruzanov.russianwives.utils.IsDataLoadingCallback;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepository implements IFirebaseRepository {

    private DatabaseReference mUserDatabaseReference;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    private StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
//    private StorageReference filePath = mImageStorage.child("profile_images").child("profile_image.jpg");

    private boolean needInfo = true;
    private boolean isDataLoading = true;

    // new db lib
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection(Consts.COLLECTION_PATH);

    private String getUid() {
        return firebaseAuth.getCurrentUser().getUid();
    }

    private String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    //method for putting to the firestore
    private void pushData(String key, String value) {
        Map<String, String> data = new HashMap<>();
        data.put(key, value);
        reference.document(getUid()).set(data);
    }

    private void getValue(String collectionName, String docName, final String valueName, final ValueCallback callback) {
        db.collection(collectionName).document(docName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    callback.getValue(snapshot.getString(valueName));
                }

            }
        });
    }

    public void getValueFromUsers(String valueName, final ValueCallback callback){
        getValue(Consts.COLLECTION_PATH, getUid(), valueName, callback);
    }

    private boolean isChanged(List<String> fieldsList) {
        boolean result = false;
        for (String field : fieldsList) {
            result = field.trim().toLowerCase().equals(Consts.DEFAULT);
        }
        return result;
    }

    private List<String> createList(DocumentSnapshot document) {
        List<String> valuesList = new ArrayList<>();
        for (String field : FirebaseRequestManager.createFieldsList()) {
            valuesList.add(document.getString(field));
        }
        return valuesList;
    }

    @Override
    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean checkingForFirstNeededInformationOfUser() {
        reference.document(getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            needInfo = isChanged(createList(document));
                            //return some data with callback
                        }
                    }
                });

        return needInfo;
    }

    @Override
    public boolean checkForUserExist() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void saveUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        Log.d("Auth", "Calling Firestore");
        db.collection(Consts.COLLECTION_PATH).document(currentUser.getUid())
                .set(FirebaseRequestManager.createNewUser(currentUser.getDisplayName(), getToken()));
    }

    @Override
    public void updateBodyTypeUserInfo(String info) {
        //getUser().child("body_type").pushData(info);
        pushData(Consts.BODY_TYPE, info);
    }

    @Override
    public void updateEthnicityTypeUserInfo(String info) {
        //getUser().child("ethnicity").pushData(info);
        pushData(Consts.ETHNICITY, info);
    }

    @Override
    public void updateHobbyUserInfo(String info) {
        //getUser().child("hobby").pushData(info);
        pushData(Consts.HOBBY, info);
    }

    @Override
    public void updateFaithUserInfo(String info) {
        //getUser().child("faith").pushData(info);
        pushData(Consts.FAITH, info);
    }

    @Override
    public void updateDrinkingUserInfo(String info) {
        //getUser().child("drink_status").pushData(info);
        pushData(Consts.DRINK_STATUS, info);
    }

    @Override
    public void updateSmokingUserInfo(String info) {
        //getUser().child("smoking_status").pushData(info);
        pushData(Consts.SMOKING_STATUS, info);
    }

    @Override
    public void updateRelationshipUserInfo(String info) {
        //getUser().child("relationship_status").pushData(info);
        pushData(Consts.RELATIONSHIP_STATUS, info);
    }

    @Override
    public void updateHaveKidsUserInfo(String info) {
        //getUser().child("number_of_kids").pushData(info);
        pushData(Consts.NUMBER_OF_KIDS, info);
    }

    @Override
    public void updateWantKidsUserInfo(String info) {
        //getUser().child("want_children_or_not").pushData(info);
        pushData(Consts.WANT_CHILDREN_OR_NOT, info);
    }

    public void searchByListParams(final List<SearchModel> searchModels, final UsersListCallback usersListCallback){
        Query query = reference.orderBy(Consts.NAME);
        for (SearchModel model: searchModels) {
            query.whereEqualTo(model.getField(), model.getValue());
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Users> usersList = new ArrayList<>();
                for(DocumentSnapshot snapshot: task.getResult().getDocuments()){
                    usersList.add(snapshot.toObject(Users.class));
                }
                usersListCallback.getUsers(usersList);
            }
        });
    }

    public void getUsersData(final UsersListCallback usersListCallback){
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Users> usersList = new ArrayList<>();
                for(DocumentSnapshot snapshot: task.getResult().getDocuments()){
                    usersList.add(snapshot.toObject(Users.class));
                }
                usersListCallback.getUsers(usersList);
            }
        });
    }

    public Query getUsersQuery(){
        return reference.orderBy(Consts.NAME, Query.Direction.ASCENDING);
    }

    @Override
    public void getUsers(String nodeId, UsersListCallback usersListCallback, IsDataLoadingCallback isDataLoadingCallback) {
       /* Query query = mUserDatabaseReference.limitToFirst(10);

        if (nodeId != null) {
            Log.d("Pagination", "Not null");
            query = query.startAt(nodeId);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isDataLoadingCallback.isDataLoading(true);
                //isDataLoading = true;
                Log.d("Pagination", "After setting true");
                List<Users> usersList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Log.d("Pagination", userSnapshot.getValue(Users.class).getName());
                    usersList.add(userSnapshot.getValue(Users.class));
                }

                usersListCallback.getUsers(usersList);
                Log.d("Pagination", "Users list was filled and contains of " +
                        String.valueOf(usersList.size()) + " items");
                isDataLoadingCallback.isDataLoading(false);
                Log.d("Pagination", "After setting false");
                //isDataLoading = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isDataLoading = false;
            }
        });*/
    }

    @Override
    public boolean isDataLoading() {
        return isDataLoading;
    }

    @Override
    public void setDataLoading(boolean isDataLoading) {
        this.isDataLoading = isDataLoading;
    }

    public void searchByName(final String searchName) {
        mUserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ///List<Users> usersList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String foundName = userSnapshot.child("name").getValue(String.class);
                    if (foundName != null && foundName.contains(searchName)) {
                        Log.d("Search", "We found: " + foundName);
                    }
                    //usersList.add(userSnapshot.getValue(Users.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void insertImageInStorage(Uri resultUri) {
        /*filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });*/
    }
}
