package com.borisruzanov.russianwives.mvp.model.repository.search;

import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.isUserExist;

public class SearchRepository {

    private CollectionReference reference = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);

    /**
     * Searching data by searchModel which includes list of key-values
     *
     * @param searchModels
     * @param usersListCallback
     */
    public void searchByListParams(final List<SearchModel> searchModels, final UsersListCallback usersListCallback) {
        Query query = reference;

        if (!searchModels.isEmpty()) {
            for (SearchModel searchModel : searchModels) {
                query = query.whereEqualTo(searchModel.getKey(), searchModel.getValue());
            }
            query.get().addOnCompleteListener(task -> putCallbackData(usersListCallback, task));
        } else query.get().addOnCompleteListener(task -> putCallbackData(usersListCallback, task));
    }

    /**
     * Util method not to copy information
     *
     * @param usersListCallback
     * @param task
     */
    private void putCallbackData(final UsersListCallback usersListCallback, Task<QuerySnapshot> task) {
        List<FsUser> fsUserList = new ArrayList<>();
        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
            if (isUserExist()) {
                if (!snapshot.getId().equals(getUid())) {
                    fsUserList.add(snapshot.toObject(FsUser.class));
                }
            } else {
                fsUserList.add(snapshot.toObject(FsUser.class));
            }
        }
        usersListCallback.setUsers(fsUserList);
    }

}
