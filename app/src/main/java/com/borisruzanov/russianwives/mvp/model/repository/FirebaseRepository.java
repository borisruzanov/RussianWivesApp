package com.borisruzanov.russianwives.mvp.model.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.Refactor.FirebaseRequestManager;
import com.borisruzanov.russianwives.utils.StringsCallback;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
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
    private CollectionReference reference = db.collection(Consts.COLLECTION_USERS);

    public void getFieldFromCurrentUser(String valueName, final ValueCallback callback) {
        getData(Consts.COLLECTION_USERS, getUid(), valueName, callback);
    }

    public void getAllInfoCurrentUser(final UserCallback callback) {
        getDocRef(Consts.COLLECTION_USERS, getUid()).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        callback.getUser(snapshot.toObject(FsUser.class));
                    }
                });
    }

    public void updateFieldFromCurrentUser(Map<String, Object> map, UpdateCallback callback) {
        updateData(Consts.COLLECTION_USERS, getUid(), map, callback);
    }


    /**
     * //     * Get list of all users
     * //     * @param usersListCallback
     * //
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
    public boolean checkForUserExist() {
        return firebaseAuth.getCurrentUser() != null;
    }
//
//    //TODO REFACTOR <-------------

    /**
     * Saving FsUser to data base
     */
    public void saveUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        db.collection(Consts.COLLECTION_USERS).document(currentUser.getUid())
                .set(FirebaseRequestManager.createNewUser(currentUser.getDisplayName(), getDeviceToken(), getUid()));
        realtimeReference.child("Users").child(currentUser.getUid()).child("created").setValue("registered");
    }

    public void getDataFromNeededFriend(String userId, String valueName, final ValueCallback callback) {
        getData(Consts.COLLECTION_USERS, userId, valueName, callback);
    }

    public void getFriendsData(String collectionName, String docName, final UserCallback userCallback) {
        getDocRef(collectionName, docName).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                Log.d("check", "exist");
                userCallback.getUser(snapshot.toObject(FsUser.class));
            }

        });
    }

    public void updateUserDataTierTwo(String firstCollection, String firstDocument, String secondCollection,
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
    }


    public void getNeededUsers(List<String> uidList, UsersListCallback usersListCallback) {
        CollectionReference users = db.collection(Consts.COLLECTION_USERS);

        //query.whereEqualTo("uid", uidList.get(0)).whereEqualTo("uid", uidList.get(1));

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


    //      Query query = db.collection(Consts.COLLECTION_USERS).whereEqualTo("uid", uidList);
//      query.get().addOnCompleteListener(task -> usersListCallback.getUsers(task.getResult().toObjects(FsUser.class)));

    //==========================================================================================
    //==========================================================================================
    //======================================w====================================================
    //=============================PRIVATE METHODS==============================================
    //==========================================================================================
    //==========================================================================================
    //==========================================================================================
    //==========================================================================================
    private void getData(String collectionName, String docName, final String valueName,
                         final ValueCallback callback) {
        getDocRef(collectionName, docName).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                callback.getValue(snapshot.getString(valueName));
            }

        });
    }

    private void updateData(String collectionName, String
            docName, Map<String, Object> map, UpdateCallback callback) {
        getDocRef(collectionName, docName).update(map).addOnCompleteListener(task -> callback.onUpdate());
    }


    /**
     * Return document reference
     *
     * @param collectionName
     * @param docName
     * @return
     */
    private DocumentReference getDocRef(String collectionName, String docName) {
        return db.collection(collectionName).document(docName);
    }

    private DocumentReference getDocRefTwo(String collectionName, String docName, String
            collName, String doccName) {
        return db.collection(collectionName).document(docName).collection(collName).document(doccName);
    }


    // ================================================================
    // <===============================Rx begin=======================>
    // ================================================================
//    /**
//     * Updating needed field of current user
//     * @param objectHashMap
//     * @return
//     */
//    public Completable updateDataOfCurrentUser(final Map<String, Object> objectHashMap){
//        return updateData(Consts.COLLECTION_USERS, getOnline(), objectHashMap);
//    }
//
//    /**
//     * Return needed value from current user
//     * @param valueName
//     * @return
//     */
//    public Observable<String> getDataFromUsers(final String valueName){
//        return getData(Consts.COLLECTION_USERS, getOnline(), valueName);
//    }
//
//    public Observable<FsUser> getCurrentUserData(){
//        return RxFirestore.getDocument(getDocRef(Consts.COLLECTION_USERS, getOnline()))
//                .filter(DocumentSnapshot::exists)
//                .toObservable()
//                .map(documentSnapshot -> documentSnapshot.toObject(FsUser.class));
//    }
//


    //
//    //TODO REFACTOR <-------------
//    /**
//     * Search mechanism
//     * @param searchModels
//     * @param usersListCallback
////     */
    public void searchByListParams(final List<SearchModel> searchModels, final UsersListCallback usersListCallback) {
        Query query = reference;

        for (SearchModel searchModel : searchModels) {
            query = query.whereEqualTo(searchModel.getKey(), searchModel.getValue());
        }
        /*for (int i = 0; i < searchModels.size(); i++) {
            query = query.whereEqualTo(searchModels.get(i).getKey(), searchModels.get(i).getValue());
        }*/

        query.get().addOnCompleteListener(task -> putCallbackData(usersListCallback, task));

    }

    private void putCallbackData(final UsersListCallback usersListCallback, Task<QuerySnapshot> task) {
        List<FsUser> fsUserList = new ArrayList<>();
        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
            fsUserList.add(snapshot.toObject(FsUser.class));
        }
        usersListCallback.getUsers(fsUserList);
    }

    //
//    //TODO REFACTOR <-------------
//
//
//    //TODO REFACTOR <-------------
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
//
//

//
//

    /**
     * Get Device Token of Current user
     *
     * @return
     */
    private String getDeviceToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }
//

//
//    /**
//     * Update data in existing table
//     * @param collectionName
//     * @param docName
//     * @param objectHashMap
//     * @return
//     */
//    private Completable updateData(String collectionName, String docName, final Map<String, Object> objectHashMap){
//        return RxFirestore.updateDocument(getDocRef(collectionName, docName), objectHashMap);
//    }
//
//
//    /**
//     * Return single value from current user
//     * @param collectionName
//     * @param docName
//     * @param valueName
//     * @return
//     */
//    private Observable<String> getData(String collectionName, String docName, final String valueName) {
//        return RxFirestore.getDocument(getDocRef(collectionName, docName))
//                .filter(DocumentSnapshot::exists)
//                .toObservable()
//                .map(documentSnapshot -> documentSnapshot.getString(valueName));
//    }
//
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

}
