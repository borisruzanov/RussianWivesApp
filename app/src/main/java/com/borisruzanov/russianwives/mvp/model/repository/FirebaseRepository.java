package com.borisruzanov.russianwives.mvp.model.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.Refactor.FirebaseRequestManager;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepository {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();

//    private StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
//    private StorageReference filePath = mImageStorage.child("profile_images").child("profile_image.jpg");

    private boolean needInfo = true;

    // new db lib
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection(Consts.USERS_DB);


    /**
     * Getting needed field from current user profile in FsUsers
     * @param valueName
     * @param callback
     */
    public void getFieldFromCurrentUser(String valueName, final ValueCallback callback) {
        getData(Consts.USERS_DB, getUid(), valueName, callback);
    }

    /**
     * Getting all datasnapshot from current user profile in FsUsers
     * @param callback
     */
    public void getAllInfoCurrentUser(final UserCallback callback) {
        getDocRef(Consts.USERS_DB, getUid()).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        callback.getUser(snapshot.toObject(FsUser.class));
                    }
                });
    }

    /**
     * Updating field information of current user in FsUsers
     * @param map
     * @param callback
     */
    public void updateFieldFromCurrentUser(Map<String, Object> map, UpdateCallback callback) {
        updateData(Consts.USERS_DB, getUid(), map, callback);
    }


    /**
     * Get list of all users
     * @param usersListCallback
     */
    public void getUsersData(final UsersListCallback usersListCallback) {
        reference.get().addOnCompleteListener(task -> {
            List<FsUser> fsUserList = new ArrayList<>();
            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                fsUserList.add(snapshot.toObject(FsUser.class));
            }
            usersListCallback.getUsers(fsUserList);
        });
    }

    /**
     * Get UID of Current user
     *
     * @return
     */
    public String getUid() {
        return firebaseAuth.getCurrentUser().getUid();
    }

    /**
     * Checking for user exist
     *
     * @return
     */
    public boolean isUserExist() {
        return firebaseAuth.getCurrentUser() != null;
    }

    /**
     * Saving FsUser to data base
     */
    //TODO поменять название баз данных на RtUsers / FsUsers
    public void saveUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        db.collection(Consts.USERS_DB).document(currentUser.getUid())
                .set(FirebaseRequestManager.createNewUser(currentUser.getDisplayName(), getDeviceToken(), getUid()));
        realtimeReference.child(Consts.USERS_DB).child(currentUser.getUid()).child("created").setValue("registered");
    }

    /**
     * Getting single value from need friend
     * @param userId
     * @param valueName
     * @param callback
     */
    public void getDataFromNeededFriend(String userId, String valueName, final ValueCallback callback) {
        getData(Consts.USERS_DB, userId, valueName, callback);
    }

    /**
     * Getting friend's object with needed data
     * @param collectionName
     * @param docName
     * @param userCallback
     */
    public void getFriendsData(String collectionName, String docName, final UserCallback userCallback) {
        getDocRef(collectionName, docName).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                Log.d("check", "exist");
                userCallback.getUser(snapshot.toObject(FsUser.class));
            }

        });
    }

    /**
     * Get list of user objects from FsUers based on list of uid
     * @param uidList
     * @param usersListCallback
     */
    public void getNeededUsers(List<String> uidList, UsersListCallback usersListCallback) {
        CollectionReference users = db.collection(Consts.USERS_DB);
        for (int j = 0; j < uidList.size(); j++) {
            users.whereEqualTo("uid", uidList.get(j))
                    .get()
                    .addOnCompleteListener(task -> {
                        List<FsUser> fsUserList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                            fsUserList.add(snapshot.toObject(FsUser.class));
                            Log.d(Contract.TAG, "Добавляем объект юзер из снэпшота и его имя " + snapshot.getString("name"));
                        }
                        for (FsUser fsUser : fsUserList) {
                            Log.d(Contract.TAG, "Имя добавленного объекта юзера в листе " + fsUser.getName());
                        }
                        usersListCallback.getUsers(fsUserList);
                    });
        }
    }

    /**
     * Searching by searchModel which includes list of key-value
     * @param searchModels
     * @param usersListCallback
     */
    public void searchByListParams(final List<SearchModel> searchModels, final UsersListCallback usersListCallback) {
        Query query = reference;

        if(!searchModels.isEmpty()) {
            for (SearchModel searchModel : searchModels) {
                query = query.whereEqualTo(searchModel.getKey(), searchModel.getValue());
            }
            query.get().addOnCompleteListener(task -> putCallbackData(usersListCallback, task));
        }
        else query.get().addOnCompleteListener(task -> putCallbackData(usersListCallback, task));
    }

    /**
     * Adding activity type in the table for user activities
     * @param userUid
     * @param friendUid
     */
    //TODO Проверить на работоспособность
    public void addUserToActivity(String userUid, String friendUid){
        Map<String, Object> chatAddMap = new HashMap<>();
        chatAddMap.put(Consts.ACTION, "visit");
        chatAddMap.put(Consts.TIMESTAMP, ServerValue.TIMESTAMP);
        chatAddMap.put(Consts.UID, userUid);
        DatabaseReference activityDb =realtimeReference.child(Consts.ACTIONS_DB).child(friendUid).push();
        //realtimeReference.child(Consts.ACTIONS_DB).child(friendUid).push();
        activityDb.updateChildren(chatAddMap);

        activityDb.child(Consts.ACTIONS_DB).child(friendUid).child(userUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> chatUserMap = new HashMap<>();
                        chatUserMap.put("Chat/" + userUid + "/" + friendUid, chatAddMap);
                        chatUserMap.put("Chat/" + friendUid + "/" + userUid, chatAddMap);
                        activityDb.setValue(chatAddMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d(Contract.TAG, "initializeChat Error is "
                                            + databaseError.getMessage().toString());
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    /**
     * Checking for information from FsUsers of current user
     * @return
     */
    //TODO Дописать метод
    public boolean checkingForFirstNeededInformationOfUser() {
        reference.document(getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            needInfo = isChanged(createList(document));
                            //return some data with callback
                        }
                    }
                });

        return needInfo;
    }

    //==========================================================================================
    //==========================================================================================
    //=============================PRIVATE METHODS==============================================
    //==========================================================================================
    //==========================================================================================
    //==========================================================================================
    //==========================================================================================

    /**
     * Getting single field from Firestore
     * @param collectionName
     * @param docName
     * @param valueName
     * @param callback
     */
    private void getData(String collectionName, String docName, final String valueName,
                         final ValueCallback callback) {
        getDocRef(collectionName, docName).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                callback.getValue(snapshot.getString(valueName));
            }

        });
    }

    /**
     * Updating signle value field in Firestore
     * @param collectionName
     * @param docName
     * @param map
     * @param callback
     */
    private void updateData(String collectionName, String
            docName, Map<String, Object> map, UpdateCallback callback) {
        getDocRef(collectionName, docName).update(map).addOnCompleteListener(task -> callback.onUpdate());
    }


    /**
     * Return document reference
     * @param collectionName
     * @param docName
     * @return
     */
    private DocumentReference getDocRef(String collectionName, String docName) {
        return db.collection(collectionName).document(docName);
    }

    /**
     * Util method not to copy information
     * @param usersListCallback
     * @param task
     */
    private void putCallbackData(final UsersListCallback usersListCallback, Task<QuerySnapshot> task) {
        List<FsUser> fsUserList = new ArrayList<>();
        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
            if (!snapshot.getId().equals(getUid())) {
                fsUserList.add(snapshot.toObject(FsUser.class));
            }
        }
        usersListCallback.getUsers(fsUserList);
    }

    /**
     * Get Device Token of Current user
     *
     * @return
     */
    private String getDeviceToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }


//
//    private boolean isChanged(List<String> fieldsList) {
//        boolean result = false;
//        for (String field : fieldsList) {
//            result = field.trim().toLowerCase().equals(Consts.DEFAULT);
//        }
//        return result;
//    }
//
//    private List<String> createList(DocumentSnapshot document) {
//        List<String> valuesList = new ArrayList<>();
//        for (String field : FirebaseRequestManager.createFieldsList()) {
//            valuesList.add(document.getString(field));
//        }
//        return valuesList;
//    }
//
//
//
//
//
//    public void insertImageInStorage(Uri resultUri) {
//        /*filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()) {
//
//                } else {
//
//                }
//            }
//        });*/
//    }
//



    /*   public void updateUserDataTierTwo(String firstCollection, String firstDocument, String secondCollection,
                                      String secondDocument, Map<String, Object> map, UpdateCallback callback) {
        getDocRefTwo(firstCollection, firstDocument, secondCollection, secondDocument).set(map)
                .addOnCompleteListener(task -> callback.onUpdate());
    }


    public void getUserDataTierTwo(String firstCollection, String firstDocument, String secondCollection,
                                   StringsCallback stringsCallback) {
        db.collection(firstCollection).document(firstDocument).collection(secondCollection).get()
                .addOnCompleteListener(task -> {
                    List<String> stringList = new ArrayList<>();
                    List<DocumentSnapshot> snapshotList = task.getResult().getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        stringList.add(snapshot.getId());
                    }
                    stringsCallback.getStrings(stringList);
                });
    }*/

    /* private DocumentReference getDocRefTwo(String collectionName, String docName, String
            collName, String doccName) {
        return db.collection(collectionName).document(docName).collection(collName).document(doccName);
    }*/
}
