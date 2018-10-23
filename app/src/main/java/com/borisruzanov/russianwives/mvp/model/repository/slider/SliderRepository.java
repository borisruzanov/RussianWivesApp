package com.borisruzanov.russianwives.mvp.model.repository.slider;

import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class SliderRepository {

    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);

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
