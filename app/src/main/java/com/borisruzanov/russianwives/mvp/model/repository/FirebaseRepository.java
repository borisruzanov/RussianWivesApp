package com.borisruzanov.russianwives.mvp.model.repository;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.FirebaseRequestManager;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class FirebaseRepository {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    private StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
//    private StorageReference filePath = mImageStorage.child("profile_images").child("profile_image.jpg");

    private boolean needInfo = true;

    // new db lib
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection(Consts.COLLECTION_PATH);

    /**
     * Updating needed field of current user
     * @param objectHashMap
     * @return
     */
    public Completable updateDataOfCurrentUser(final Map<String, Object> objectHashMap){
        return updateData(Consts.COLLECTION_PATH, getUid(), objectHashMap);
    }

    /**
     * Return needed value from current user
     * @param valueName
     * @return
     */
    public Observable<String> getDataFromUsers(final String valueName){
        return getData(Consts.COLLECTION_PATH, getUid(), valueName);
    }

    public Observable<User> getCurrentUserData(){
        return RxFirestore.getDocument(getDocRef(Consts.COLLECTION_PATH, getUid()))
                .filter(DocumentSnapshot::exists)
                .toObservable()
                .map(documentSnapshot -> documentSnapshot.toObject(User.class));
    }

    /**
     * Checking for user exist
     * @return
     */
    public boolean checkForUserExist() {
        return firebaseAuth.getCurrentUser() != null;
    }

    //TODO REFACTOR <-------------
    /**
     * Saving User to data base
     */
    public void saveUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        db.collection(Consts.COLLECTION_PATH).document(currentUser.getUid())
                .set(FirebaseRequestManager.createNewUser(currentUser.getDisplayName(), getDeviceToken()));
    }

    //TODO REFACTOR <-------------
    /**
     * Search mechanism
     * @param searchModels
     * @param usersListCallback
     */
    public void searchByListParams(final List<SearchModel> searchModels, final UsersListCallback usersListCallback){
        Query query = reference.orderBy(Consts.NAME);
        for (SearchModel model: searchModels) {
            query.whereEqualTo(model.getField(), model.getValue());
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> userList = new ArrayList<>();
                for(DocumentSnapshot snapshot: task.getResult().getDocuments()){
                    userList.add(snapshot.toObject(User.class));
                }
                usersListCallback.getUsers(userList);
            }
        });
    }

    //TODO REFACTOR <-------------
    /**
     * Get list of all users
     * @param usersListCallback
     */
    public void getUsersData(final UsersListCallback usersListCallback){
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> userList = new ArrayList<>();
                for(DocumentSnapshot snapshot: task.getResult().getDocuments()){
                    userList.add(snapshot.toObject(User.class));
                }
                usersListCallback.getUsers(userList);
            }
        });
    }

    //TODO REFACTOR <-------------
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


    /**
     * Get UID of Current user
     * @return
     */
    private String getUid() {
        return firebaseAuth.getCurrentUser().getUid();
    }


    /**
     * Get Device Token of Current user
     * @return
     */
    private String getDeviceToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    /**
     * Return document reference
     * @param collectionName
     * @param docName
     * @return
     */
    private DocumentReference getDocRef(String collectionName, String docName){
        return db.collection(collectionName).document(docName);
    }

    /**
     * Update data in existing table
     * @param collectionName
     * @param docName
     * @param objectHashMap
     * @return
     */
    private Completable updateData(String collectionName, String docName, final Map<String, Object> objectHashMap){
        return RxFirestore.updateDocument(getDocRef(collectionName, docName), objectHashMap);
    }


    /**
     * Return single value from current user
     * @param collectionName
     * @param docName
     * @param valueName
     * @return
     */
    private Observable<String> getData(String collectionName, String docName, final String valueName) {
        return RxFirestore.getDocument(getDocRef(collectionName, docName))
                .filter(DocumentSnapshot::exists)
                .toObservable()
                .map(documentSnapshot -> documentSnapshot.getString(valueName));
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
