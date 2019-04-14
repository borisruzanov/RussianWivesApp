package com.borisruzanov.russianwives.mvp.model.repository.slider;

import android.net.Uri;
import android.util.Log;

import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class SliderRepository {

    private CollectionReference users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);
    private DatabaseReference usersRt = FirebaseDatabase.getInstance().getReference().child(Consts.USERS_DB);
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    /**
     * Getting needed field from current user profile in FsUsers
     *
     * @param valueName
     * @param callback
     */
    public void getFieldFromCurrentUser(String valueName, final ValueCallback callback) {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                callback.setValue(snapshot.getString(valueName));
            }
        });
    }

    /**
     * Updating field information of current user in FsUsers
     *
     * @param map
     * @param callback
     */
    public void updateFieldFromCurrentUser(Map<String, Object> map, UpdateCallback callback) {
        users.document(getUid()).update(map).addOnCompleteListener(task -> callback.onUpdate());
    }

    public void uploadUserPhoto(Uri photoUri, UpdateCallback endCallback) {
        StorageReference filePath = storageReference.child("profile_images").child(getUid()).child("profile_photo");
        filePath.putFile(photoUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            // Continue with the task to get the download URL
            return filePath.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String download_url = task.getResult().toString();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(Consts.IMAGE, download_url);

                usersRt.child(getUid()).child(Consts.IMAGE).setValue(download_url);
                updateFieldFromCurrentUser(hashMap, endCallback);
            }
        });
    }


    /*
    private void getData(String collectionName, String docName, final String valueName,
                         final ValueCallback callback) {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                callback.setValue(snapshot.getString(valueName));
            }

        });
    }

    /**
     * Updating signle value field in Firestore
     *
     * @param collectionName
     * @param docName
     * @param map
     * @param callback
     *//*
    private void updateData(String collectionName, String
            docName, Map<String, Object> map, UpdateCallback callback) {
        getDocRef(collectionName, docName).update(map).addOnCompleteListener(task -> callback.onUpdate());
    }

    private DocumentReference getDocRef(String collectionName, String docName) {
        return db.collection(collectionName).document(docName);
    }*/

}
