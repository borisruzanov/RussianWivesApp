package com.borisruzanov.russianwives.mvp.model.repository.user;

import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.utils.FirebaseRequestManager;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.models.ActionModel;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.utils.ActionCallback;
import com.borisruzanov.russianwives.utils.ActionCountCallback;
import com.borisruzanov.russianwives.utils.ActionItemCallback;
import com.borisruzanov.russianwives.utils.ActionsCountInfoCallback;
import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.StringsCallback;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.FULL_PROFILE_ACH;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getDeviceToken;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUsers;

public class UserRepository {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);
    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();

    private Prefs prefs;

    public UserRepository(Prefs prefs) {
        this.prefs = prefs;
    }

    /**
     * Saving FsUser to data base
     */
    public void saveUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        users.get().addOnCompleteListener(task -> {
            List<String> uidList = new ArrayList<>();

            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                uidList.add(snapshot.getString(Consts.UID));
            }

            if (!uidList.contains(getUid())) {
                createNewUser(currentUser.getDisplayName(), getDeviceToken(), getUid());
            } else {
                updateToken();
            }

        });
    }

    public void addFullProfileUsers() {
        users.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot snapshot: task.getResult().getDocuments()) {
                if (getListOfDefaults(snapshot).isEmpty()) {
                    String uid = snapshot.getId();
                    Log.d("FpDebug", "User with full profile uid is " + uid);
                    Map<String, Object> fpMap = new HashMap<>();
                    fpMap.put(FULL_PROFILE_ACH, "true");
                    users.document(uid).update(fpMap);
                    new RatingRepository().addAchievement(FULL_PROFILE_ACH);
                }
            }
        });
/*        realtimeReference.child(Consts.USERS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    Log.d("FpDebug", "User uid is " + uid);
                    new RatingRepository().isAchExist(uid, FULL_PROFILE_ACH, flag -> {
                        if (flag) {
                            Log.d("FpDebug", "User with full profile uid is " + uid);
                            Map<String, Object> fpMap = new HashMap<>();
                            fpMap.put(FULL_PROFILE_ACH, "true");
                            users.document(uid).update(fpMap);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });*/
    }

    public void hasMustInfo(BoolCallback callback) {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                DocumentSnapshot snapshot = task.getResult();
                String image = snapshot.getString(Consts.IMAGE);
                String age = snapshot.getString(Consts.AGE);
                String country = snapshot.getString(Consts.COUNTRY);
                boolean value = !image.equals(Consts.DEFAULT) &&  !age.equals(Consts.DEFAULT) && !country.equals(Consts.DEFAULT);
                callback.setBool(value);
            }
        });
    }

    public void setDialogLastOpenDate() {
        prefs.setDialogOpenDate();
    }

    public void setFPDialogLastOpenDate() {
        prefs.setFPOpenDate();
    }

    private boolean isOneDayGone(String key) {
        float days = 0f;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date date1 = dateFormat.parse(prefs.getValue(key));
                Date date2 = Calendar.getInstance().getTime();
                long diff = date2.getTime() - date1.getTime();
                days = (diff / (1000*60*60*24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return prefs.getValue(key).equals(Consts.DEFAULT) || (days >= 1 && !prefs.getValue(key).equals(""));
    }

    public void updateToken() {
        Map<String, Object> ntMap = new HashMap<>();
        ntMap.put(Consts.DEVICE_TOKEN, getDeviceToken());
        users.document(getUid()).update(ntMap);
        realtimeReference.child(Consts.USERS_DB).child(getUid()).updateChildren(ntMap);
    }

    private void createNewUser(String name, String token, String uid) {
        users.document(uid).set(FirebaseRequestManager.createNewUser(name, token, uid, prefs.getUserGender()));

        Map<String, Object> niMap = new HashMap<>();
        niMap.put("created", "registered");
        niMap.put(Consts.DEVICE_TOKEN, token);
        niMap.put(Consts.IMAGE, "default");
        niMap.put(Consts.NAME, name);
        niMap.put(Consts.RATING, 1);
        niMap.put(Consts.ACHIEVEMENTS, new ArrayList<String>());
        niMap.put(Consts.ONLINE, ServerValue.TIMESTAMP);
        niMap.put(Consts.UID, uid);
        realtimeReference.child(Consts.USERS_DB).child(uid).setValue(niMap);
    }

    public void setFirstOpenDate(){
        if (prefs.getFirstOpenDate().equals(Consts.DEFAULT)) {
            prefs.setFirstOpenDate();
        }
    }

    public boolean isGenderDefault() {
        return prefs.getGenderSearch().equals(Consts.DEFAULT);
    }

    public void setGender(String gender) {
        if (!gender.equals(Consts.DEFAULT)) {
            prefs.setGenderSearch(gender);
        }
    }

    public void hasFullProfileInfo(BoolCallback callback) {
        new RatingRepository().isAchievementExist(FULL_PROFILE_ACH, flag -> {
            if (flag) {
                prefs.clearValue(Consts.FP_OPEN_DATE);
                callback.setBool(false);
            }
            else callback.setBool(isOneDayGone(Consts.FP_OPEN_DATE));
        });
    }

    public void hasNecessaryInfo(BoolCallback callback) {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                DocumentSnapshot snapshot = task.getResult();
                new RatingRepository().isAchievementExist(FULL_PROFILE_ACH, flag -> {
                    if (flag) {
                        prefs.clearValue(Consts.DIALOG_OPEN_DATE);
                        callback.setBool(false);
                    }
                    else callback.setBool(isOneDayGone(Consts.DIALOG_OPEN_DATE) &&
                            !snapshot.getString(Consts.GENDER).equals(Consts.DEFAULT));
                });
            }
        });
    }

    public void setFullProfile() {
        Map<String, Object> fpMap = new HashMap<>();
        fpMap.put(FULL_PROFILE_ACH, "true");
        users.document(getUid()).update(fpMap);
    }

    public void getDefaultList(StringsCallback callback) {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            callback.setStrings(getListOfDefaults(snapshot));
        });
    }

    public boolean isUserExist() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser != null;
    }

    public void getAllCurrentUserInfo(final UserCallback callback) {
        users.document(getUid()).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        callback.setUser(snapshot.toObject(FsUser.class));
                    }
                });
    }

    public void getTransformedActions(ActionItemCallback callback) {
        List<ActionItem> actionItems = new ArrayList<>();
        getMergedActions(actionModels -> {
            List<String> uidList = new ArrayList<>();
            for (ActionModel model : actionModels) {
                uidList.add(model.getUid());
            }

            getUsers(uidList, fsUserList -> {
                for (int i = 0; i < fsUserList.size(); i++) {
                    actionItems.add(new ActionItem(actionModels.get(i).getUid(), fsUserList.get(i).getName(),
                            fsUserList.get(i).getImage(), actionModels.get(i).getType(),
                            actionModels.get(i).getTimestamp()));
                }
                callback.setActionItems(actionItems);
            });
        });
    }

    public void getActionsCountInfo(ActionsCountInfoCallback callback) {
        getActionCount("Visits", visitsCount ->
                getActionCount("Likes", likesCount -> callback.setActions(visitsCount, likesCount)));
    }

    private void getLikes(ActionCallback callback) {
        realtimeReference.child("Likes").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ActionModel> actionModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    actionModels.add(new ActionModel(
                            Long.valueOf(snapshot.child("timestamp").getValue().toString()),
                            snapshot.getKey(),
                            "like"));
                }
                callback.setActionList(actionModels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ActionList", "getLikes" + databaseError.getMessage());
            }
        });
    }

    private void getVisits(ActionCallback callback) {
        realtimeReference.child("Visits").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ActionModel> actionModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Long timestamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());
                    String fromUid = snapshot.child("fromUid").getValue().toString();
                    actionModels.add(new ActionModel(timestamp, fromUid, "visit"));
                }
                callback.setActionList(actionModels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ActionList", "getVisits" + databaseError.getMessage());
            }
        });
    }

    public void addUid() {
        realtimeReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    Log.d("RDUpdate", "Uid is " + uid);
                    Map<String, Object> uidMap = new HashMap<>();
                    uidMap.put(Consts.UID, uid);
                    snapshot.getRef().updateChildren(uidMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("RDUpdate", "Error message is " + databaseError.getMessage());
            }
        });
        /*users.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                if (snapshot.getString(Consts.UID) == null) {
                    Map<String, Object> uidMap = new HashMap<>();
                    uidMap.put(Consts.UID, snapshot.getId());
                    snapshot.getReference().update(uidMap);
                }
            }
        });*/
    }

    public void addInfo() {
        users.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                Map<String, Object> niMap = new HashMap<>();
                niMap.put("created", "registered");
                niMap.put("device_token", snapshot.getString("device_token"));
                niMap.put(Consts.IMAGE, snapshot.getString(Consts.IMAGE));
                niMap.put(Consts.NAME, snapshot.getString(Consts.NAME));
                niMap.put("online", ServerValue.TIMESTAMP);
                realtimeReference.child("Users").child(snapshot.getId()).setValue(niMap);
            }
        });
    }

    public void makeDialogOpenDateDefault () {
        prefs.setDialogOpenDate(Consts.DEFAULT);
    }
    public void clearDialogOpenDate() {
        prefs.clearValue(Consts.DIALOG_OPEN_DATE);
    }

    private void getMergedActions(ActionCallback callback) {
        List<ActionModel> actionModels = new ArrayList<>();
        getLikes(actionLikeModels -> getVisits(actionVisitModels -> {
            actionModels.addAll(actionLikeModels);
            actionModels.addAll(actionVisitModels);
            Collections.sort(actionModels, (e1, e2) -> Long.compare(e2.getTimestamp(), e1.getTimestamp()));
            callback.setActionList(actionModels);
        }));
    }

    private void getActionCount(String dbName, ActionCountCallback callback) {
        realtimeReference.child(dbName).child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.setActionCount(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private List<String> getListOfDefaults(DocumentSnapshot document) {
        List<String> valuesList = new ArrayList<>();
        for (String field : Consts.fieldKeyList) {
            String value = document.getString(field);
            if (value != null && value.toLowerCase().trim().equals(Consts.DEFAULT)) {
                valuesList.add(field);
            }
        }
        return valuesList;
    }

}
