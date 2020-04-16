package com.borisruzanov.russianwives.mvp.model.repository.user;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.borisruzanov.russianwives.eventbus.BooleanEvent;
import com.borisruzanov.russianwives.eventbus.FakeUsersListEvent;
import com.borisruzanov.russianwives.eventbus.ListStringEvent;
import com.borisruzanov.russianwives.eventbus.RealUsersListEvent;
import com.borisruzanov.russianwives.eventbus.SearchEvent;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.eventbus.VisitsEvent;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.models.ActionModel;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.models.OnlineUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.utils.ActionCallback;
import com.borisruzanov.russianwives.utils.ActionCountCallback;
import com.borisruzanov.russianwives.utils.ActionItemCallback;
import com.borisruzanov.russianwives.utils.ActionsCountInfoCallback;
import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.FirebaseRequestManager;
import com.borisruzanov.russianwives.utils.OnlineUsersCallback;
import com.borisruzanov.russianwives.utils.StringsCallback;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.borisruzanov.russianwives.utils.network.ApiClient;
import com.borisruzanov.russianwives.utils.network.ApiService;
import com.borisruzanov.russianwives.utils.network.EmailErrorResponse;
import com.borisruzanov.russianwives.utils.network.EmailResponse;
import com.borisruzanov.russianwives.utils.network.TokenResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.FULL_PROFILE_ACH;
import static com.borisruzanov.russianwives.utils.Consts.ITEM_LOAD_COUNT;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getDeviceToken;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUsers;

public class UserRepository {

    private static final String TAG_CLASS_NAME = "UserRepository";
    private static final String GENDER_FEMALE = "Female";
    private static final String GENDER_MALE = "Male";
    private static final String TAG = "UserRepository";
    boolean isLoading = false;
    private String last_node = "";
    private String last_key = "";
    private long last_rating = 0;
    private String endAt = null;
    private boolean isMaxData = false;
    private String mToken; //to store token value from sendpulse
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); //custom dateformat
    private final String client_id = "fd75e73d3de24d87e6bb2227163b72b0";
    private final String client_secret = "a04e986befc1ff7995e8c69be2e7b7da";
    private final String mMaleBookId = "700899";
    private final String mFemaleBookId = "690773";
    private final String grant_type = "client_credentials";
    private String mBookId = mFemaleBookId; //store the bookid
    private String mUserEmail;
    private String mUserName;
    private String mSendpulseToken; //to store token value
    private JsonObject sendEmailObject;


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);
    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();

    private Prefs mPrefs;


    public UserRepository(Prefs mPrefs) {
        this.mPrefs = mPrefs;
    }

    /**
     * Saving FsUser to data base
     */
    public void saveUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        createNewUser(currentUser.getDisplayName(), getDeviceToken(), getUid());
    }

    /**
     * Save email of the user in send pulse
     */
    public void saveUserInSendpulse() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            mUserEmail = currentUser.getEmail();
            mUserName = currentUser.getDisplayName();
            if (mUserEmail != null) {
                if (!mUserEmail.isEmpty()) {
                    addUserToSendpulse();
                }
            }
        }
    }

    /**
     * Adding email through API call to sendpulse
     *
     * @param emails
     */
    private void sendEmail(String emails, String username) {
        if (emails.isEmpty()) {
            return;
        }
        mSendpulseToken = mPrefs.getValue(Consts.ACCESS_TOKEN);
        if (mSendpulseToken == null) {
            return;
        }
        mSendpulseToken = mPrefs.getValue(Consts.TOKEN_TYPE) + " " + mSendpulseToken;
        ApiService service = ApiClient.createService(ApiService.class, mSendpulseToken);

        //create list object to send email
        List<HashMap<String, Object>> mailHashMap = new ArrayList<>();
        HashMap<String, Object> detailsMap = new HashMap<>(); //create hashmap to put email and other values
        HashMap<String, String> variablemap = new HashMap<>();//create hasmap to put values like username and phone no ,etc.
        detailsMap.put("email", emails);//add email in detailmaop object
        variablemap.put("UserName", username);//add username in variblemap object
        //you can add multiple values in variablemap object
        detailsMap.put("variables", variablemap);
        mailHashMap.add(detailsMap);
        /*After all that the request body looks like
            emails:[
                {
                    "email":"emailid",
                    "variables":{
                        "UserName":"Your Username"
                    }
                }
            ]
        */
        Log.d("data", mailHashMap.toString());
        if (mPrefs.getValue(Consts.GENDER).equals(Consts.MALE)){
            mBookId = mMaleBookId;
        }
        createList(emails,username);//get jsonobject with emails array
        Call<EmailResponse> responseCall=service.addEmail(mBookId, sendEmailObject);
        responseCall.enqueue(new Callback<EmailResponse>() {
            @Override
            public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {
                setResponse(response);
            }

            @Override
            public void onFailure(Call<EmailResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "EmailResponse onfailure error:- " + t.getMessage());
            }
        });
    }

    /**
     * Creating list for sending it to sendpulse
     * @param email
     * @param username
     */
    private void createList(String email, String username) {
        JsonObject variable=new JsonObject();
        variable.addProperty("Name",username);
        JsonObject emaildata = new JsonObject();
        emaildata.addProperty("email",email);
        emaildata.add("variables",variable);
        JsonArray lisJsonArray = new JsonArray();
        lisJsonArray.add(emaildata);
        sendEmailObject =new JsonObject();
        sendEmailObject.addProperty("emails",lisJsonArray.toString());
        Log.d("data",lisJsonArray.toString());
    }

    /**
     * Customize rsponse of addemail api call
     *
     * @param response
     */
    private void setResponse(Response<EmailResponse> response) {
        if (response.code() == 200) {
            mPrefs.setValue(Consts.SENDPULSE_STATUS, Consts.CONFIRMED);
            Log.d(TAG, "Sendpulse sent email success");
        } else if (response.code() == 400) {
            EmailErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), EmailErrorResponse.class);
            Log.d(TAG, "Error_code:- " + errorResponse.getError_code());
        } else {
            Log.d(TAG, "some wrong" + response.code());
            try {
                Log.d(TAG, "error message" + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get token through API call
     */
    private void addUserToSendpulse() {
        ApiService service = ApiClient.createService(ApiService.class);
        Call<TokenResponse> responseCall = service.getAuthToken(grant_type, client_id, client_secret);
        responseCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenResponse tokenResponse = response.body();
                if (tokenResponse != null) {
                    mPrefs.setValue(Consts.ACCESS_TOKEN, tokenResponse.getAccess_token());
                    mPrefs.setValue(Consts.TOKEN_TYPE, tokenResponse.getToken_type());
                    mPrefs.setValue(Consts.TOKEN_TIME, simpleDateFormat.format(new Date()));
                    mPrefs.setValue("expiresin", tokenResponse.getExpires_in());
                    Log.d(TAG, "TokenResponse details:- " + tokenResponse.toString());
                    sendEmail(mUserEmail, mUserName);
                } else {
                    Log.d(TAG, "Tokenresponse is null,check logs");
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "authtoken on failure error:-" + t.getMessage());
            }
        });
    }

    /**
     * Creating user in both databases after registration
     */
    private void createNewUser(String name, String token, String uid) {
        //Saving user in Firestore
        users.document(uid).set(FirebaseRequestManager.createNewUser(name, token, uid, mPrefs.getValue(Consts.GENDER)));

        //Saving user in Realtime
        Map<String, Object> niMap = new HashMap<>();
        niMap.put("created", "registered");
        niMap.put(Consts.DEVICE_TOKEN, token);
        niMap.put(Consts.IMAGE, "default");
        niMap.put(Consts.NAME, name);
        niMap.put(Consts.RATING, 1);
        niMap.put(Consts.ACHIEVEMENTS, new ArrayList<String>());
        niMap.put(Consts.ONLINE, ServerValue.TIMESTAMP);
        niMap.put(Consts.UID, uid);
        niMap.put(Consts.LIKES, "0");
        niMap.put(Consts.VISITS, "0");
        realtimeReference.child(Consts.USERS_DB).child(uid).setValue(niMap);
        Log.d(TAG_CLASS_NAME, "createNewUser - user created");
    }

    /**
     * Dialog scenario - Checking if user has must info in mPrefs and in DB to show dialog
     */
    public void userHasMustInfo() {
        //Checking if must info in mPrefs for saving traffic
        //Works only for first install and prefs are clear
        if (mPrefs.getMustInfo().equals(Consts.DEFAULT)) {
            users.document(getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        //TODO CRASH FIX
                        if (Objects.requireNonNull(snapshot.getString(Consts.MUST_INFO)).equals(Consts.FALSE) && snapshot.getString(Consts.MUST_INFO) != null) {
                            Log.d(TAG_CLASS_NAME, "userHasMustInfo calling for must info dialog");
                            EventBus.getDefault().post(new StringEvent(Consts.MUST_INFO));
                        } else {
                            Log.d(TAG_CLASS_NAME, "userHasMustInfo not needed");
                        }
                    }
                } else {
                    Log.d(TAG_CLASS_NAME, "userHasMustInfo task failed");
                }
            });
        } else {
            //Triggers if user logged out and creating new account
            checkForFullProfile();
        }
    }

    /**
     * Dialog scenario - checking if user has full profile - show dialog
     */
    private void checkForFullProfile() {
        if (mPrefs.getFullProfile().isEmpty() || mPrefs.getFullProfile().equals(Consts.DEFAULT)) {
            users.document(getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        if (snapshot.getString(Consts.FULL_PROFILE).equals(Consts.FALSE) && snapshot.getString(Consts.MUST_INFO).equals(Consts.TRUE)) {
                            //Triggers only in case must info is filled
                            EventBus.getDefault().post(new StringEvent(Consts.FULL_PROFILE));
                        } else if (snapshot.getString(Consts.MUST_INFO).equals(Consts.FALSE)) {
                            //This triggers only if user logged out
                            EventBus.getDefault().post(new StringEvent(Consts.MUST_INFO));
                        } else {
                            Log.d(TAG_CLASS_NAME, "checkForFullProfile profile is full");
                            EventBus.getDefault().post(new StringEvent(Consts.UPDATE_MODULE));
                        }
                    }
                } else {
                    Log.d(TAG_CLASS_NAME, "checkForFullProfile task failed");
                }
            });
        }
    }

    /**
     * Getting the list of default fields of the user from DB and sending back to the slider
     *
     * @param callback
     */
    public void getDefaultList(StringsCallback callback) {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            callback.setStrings(getListOfDefaults(snapshot));
        });
    }

    /**
     * Getting the list of default fields to let user fill them with info
     *
     * @param document - object from DB
     * @return - ready for use list
     */
    private ArrayList<String> getListOfDefaults(DocumentSnapshot document) {
        ArrayList<String> valuesList = new ArrayList<>();
        for (String field : Consts.fieldKeyList) {
            String value = document.getString(field);
            if (value != null && value.toLowerCase().trim().equals(Consts.DEFAULT)) {
                valuesList.add(field);
            }
        }
        return valuesList;
    }

    /**
     * Getting default values of the user fields
     */
    public void getDefaultFieldsList() {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            EventBus.getDefault().post(new ListStringEvent(getListOfDefaults(snapshot)));
        });
    }

    /**
     * Insert user in online users table
     *
     * @param user
     */
    public void changeUserOnlineStatus(@NotNull FsUser user) {
        // -1 because in realtime we cant make desc order that just a trick to avoid that and sort the right way
        OnlineUser onlineUser = new OnlineUser(user.getUid(), user.getName(), user.getImage(), user.getGender(), user.getCountry(), user.getRating() * -1);
        FirebaseDatabase.getInstance().getReference().child("OnlineUsers/").child(mPrefs.getValue(Consts.GENDER)).child(user.getUid()).setValue(onlineUser);

        FirebaseDatabase.getInstance().getReference().child("OnlineUsers/").child(mPrefs.getValue(Consts.GENDER)).child(user.getUid()).onDisconnect().removeValue();


    }

    /**
     * Getting fake users for unregistered users
     */
    private void callFakeUserList(String neededGender) {
        DatabaseReference mReference = realtimeReference.child("FakeUsers/" + neededGender);
        Query query = mReference.orderByChild("rank");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<OnlineUser> userList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    OnlineUser user = postSnapshot.getValue(OnlineUser.class);
                    userList.add(new OnlineUser(user.getUid(), user.getName(), user.getImage(), user.getGender(), user.getCountry(), user.getRating()));
                }
                EventBus.getDefault().post(new FakeUsersListEvent(userList));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("test", "error " + databaseError.getMessage());
//                registerErrorEvent(databaseError);
            }
        });

    }

    /**
     * Getting real online users from Realtime Database
     */
    public void getLastKeyNodeFromFirebase() {
        Query getLastKey = FirebaseDatabase.getInstance().getReference()
                .child("OnlineUsers")
                .child(getNeededGender())
                .orderByKey()
                .limitToLast(1);
        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot lastKey : dataSnapshot.getChildren()) {
                    last_key = lastKey.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Logic", "Last key error " + databaseError.getMessage());
            }
        });
    }


    /**
     * Calling for a fake / real online list based on converted gender
     *
     * @param isUserExist
     */
    public void getOnlineUsers(boolean isUserExist) {
        if (isUserExist) {
            callRealUserList();
        } else {
            //Get fake users list for unregistered user
            callFakeUserList(getNeededGender());
        }
    }

    /**
     * Getting the gender of the user convert it to opposite and return
     *
     * @return - opposite gender of the user
     */
    private String getNeededGender() {
        String neededGender;
        if (getGender().equals(GENDER_FEMALE)) {
            neededGender = GENDER_MALE;
        } else if (getGender().equals(GENDER_MALE)) {
            neededGender = GENDER_FEMALE;
        } else {
            neededGender = GENDER_FEMALE;
        }
        return neededGender;
    }

    /**
     * Make a query to get a real online user list
     */
    public void callRealUserList() {
        if (!isMaxData) {
            Query query;
            if (TextUtils.isEmpty(last_node)) {
                //First query of the list
                query = FirebaseDatabase.getInstance().getReference()
                        .child("OnlineUsers")
                        .child(getNeededGender())
                        .orderByChild("rating")
                        .limitToFirst(Consts.ITEM_LOAD_COUNT);
            } else {
                //Query based on last item
                query = FirebaseDatabase.getInstance().getReference()
                        .child("OnlineUsers")
                        .child(getNeededGender())
                        .orderByChild("rating")
                        .startAt(last_rating, last_node)
                        .limitToFirst(ITEM_LOAD_COUNT);
            }

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {

                        List<OnlineUser> newUser = new ArrayList<>();
                        for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                            newUser.add(userSnapShot.getValue(OnlineUser.class));
                        }
                        last_node = newUser.get(newUser.size() - 1).getUid();
                        last_rating = newUser.get(newUser.size() - 1).getRating();
                        if (!last_node.equals(last_key)) {
                            newUser.remove(newUser.size() - 1);
                        } else {
                            last_node = "end";
                            isMaxData = true;

                        }

                        isLoading = false;
                        EventBus.getDefault().post(new RealUsersListEvent(newUser));
                    } else {
                        isLoading = false;
                        isMaxData = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            last_node = "end";
        }
    }

    /**
     * Saving user in social media table for sharing
     *
     * @param user
     */
    public void saveInSocMed(FsUser user) {
        if (user != null) {
            if (user.getGender() != null) {
                String userGender = user.getGender();
                if (userGender.equals(Consts.FEMALE)) {
                    realtimeReference.child("SocialAccounts").child(Consts.FEMALE).child(user.getUid()).setValue(user);
                } else {
                    realtimeReference.child("SocialAccounts").child(Consts.MALE).child(user.getUid()).setValue(user);
                }
            } else {
                realtimeReference.child("SocialAccounts").child(Consts.MALE).child(user.getUid()).setValue(user);
            }

        }
    }

    public void resetPaginationValues() {
        isLoading = false;
        last_node = "";
        last_key = "";
        last_rating = 0;
        endAt = null;
        isMaxData = false;
    }

    public void addRating(double addPoint) {
        addRating(getUid(), addPoint);
    }

    public void addRating(String uid, double addPoint) {
        if (uid != null) {
            users.document(uid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    int rating = snapshot.getDouble(Consts.RATING).intValue();
                    double newRating = rating + addPoint;
                    Map<String, Object> achMap = new HashMap<>();
                    achMap.put(Consts.RATING, newRating);
                    users.document(getUid()).update(achMap);
                    mPrefs.setValue(Consts.MUST_INFO, Consts.TRUE);
                }
            });
        } else Log.d("RatingDebug", "Rating ALARM from UR");
    }

    public void roundRating() {
        users.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                int rating = 0;
                Map<String, Object> ratingMap = new HashMap<>();
                String uid = snapshot.getId();
                if (snapshot.getDouble(Consts.RATING) != null) {
                    rating = snapshot.getDouble(Consts.RATING).intValue();
                }
                ratingMap.put(Consts.RATING, rating);
                users.document(uid).update(ratingMap);
            }
        });
    }

    public void addDataToOnlineUsers() {
        realtimeReference.child(Consts.USERS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    String uid = snapshot.getKey();
                    HashMap<String, Object> onlineUsersMap = new HashMap<>();
                    onlineUsersMap.put(Consts.IMAGE, snapshot.child(Consts.IMAGE).getValue().toString());
                    onlineUsersMap.put(Consts.NAME, snapshot.child(Consts.NAME).getValue().toString());
                    onlineUsersMap.put(Consts.RATING, snapshot.child(Consts.RATING).getValue());
                    if (i < 100) {
                        realtimeReference.child("OnlineUsers").child("Test").child(uid).updateChildren(onlineUsersMap);
                    } else {
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setOnlineUsersCallback(DataSnapshot usersSnapshot, OnlineUsersCallback callback) {
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (DataSnapshot snapshot : usersSnapshot.getChildren()) {
            String uid = snapshot.getKey();
            long rating = (long) snapshot.child(Consts.RATING).getValue();
            OnlineUser onlineUser = new OnlineUser(snapshot.getKey(),
                    snapshot.child(Consts.NAME).getValue().toString(),
                    snapshot.child(Consts.IMAGE).getValue().toString(),
                    snapshot.child(Consts.GENDER).getValue().toString(),
                    snapshot.child(Consts.COUNTRY).getValue().toString(), rating);
            if (isUserExist()) {
                if (!uid.equals(getUid())) {
                    Log.d("OnlineDebug", "UID is " + uid + " and rating is " + rating);
                    onlineUsers.add(onlineUser);
                }
            } else {
                Log.d("OnlineDebug", "UID is " + uid + " and rating is " + rating);
                onlineUsers.add(onlineUser);
            }
        }
        if (onlineUsers.size() > 0) {
            endAt = onlineUsers.get(onlineUsers.size() - 1).getUid();
            Log.d("OnlineDebug", "In setCallback endAt is " + endAt);
        } else {
            Log.d("OnlineDebug", "List is empty");
        }
        callback.setOnlineUsers(onlineUsers);
    }

    public void addFieldsToRealtimeUsers() {
        realtimeReference.child(Consts.USERS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    if (snapshot.child(Consts.GENDER).getValue() == null || snapshot.child(Consts.COUNTRY).getValue() == null) {
                        if (uid != null) {
                            users.document(uid).get().addOnCompleteListener(task -> {
                                if (task.getResult().exists()) {
                                    DocumentSnapshot fs = task.getResult();
                                    HashMap<String, Object> updateMap = new HashMap<>();
                                    updateMap.put(Consts.COUNTRY, fs.getString(Consts.COUNTRY));
                                    updateMap.put(Consts.GENDER, fs.getString(Consts.GENDER));
                                    Log.d("RealtimeUsersDebug", "Update " + uid);
                                    snapshot.getRef().updateChildren(updateMap);
                                }
                            });
                        } else Log.d("RealtimeUsersDebug", "uid is null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void addOnlineUsers() {
        realtimeReference.child(Consts.USERS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // TODO add more users, add check for gender null and for default
                    String uid = snapshot.getKey();
                    if (snapshot.child(Consts.GENDER).getValue() != null && snapshot.child(Consts.NAME).getValue() != null) {
                        String gender = snapshot.child(Consts.GENDER).getValue().toString();
                        if (gender.equals("Male") || gender.equals("Female")) {
                            HashMap<String, Object> onlineUserMap = new HashMap<>();
                            onlineUserMap.put(Consts.IMAGE, Objects.requireNonNull(snapshot.child(Consts.IMAGE).getValue()).toString());
                            onlineUserMap.put(Consts.NAME, Objects.requireNonNull(snapshot.child(Consts.NAME).getValue()).toString());
                            onlineUserMap.put(Consts.RATING, snapshot.child(Consts.RATING).getValue());
                            onlineUserMap.put(Consts.COUNTRY, Objects.requireNonNull(snapshot.child(Consts.COUNTRY).getValue()).toString());
                            onlineUserMap.put(Consts.UID, Objects.requireNonNull(snapshot.child(Consts.UID).getValue()).toString());
                            onlineUserMap.put(Consts.GENDER, gender);
                            Log.d("OnlineUsersDebug", "Add user to OnlineUsers" + "/" + gender + "/" + uid);
                            realtimeReference.child("OnlineUsers").child(gender).child(uid).updateChildren(onlineUserMap);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addFullProfileUsers() {
//        users.get().addOnCompleteListener(task -> {
//            Long tsLong = System.currentTimeMillis() / 1000;
//
//            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                String uid = snapshot.getId();
//
//                Map<String, Object> fpMap = new HashMap<>();
//                fpMap.put(Consts.ID_SOC, tsLong.toString());
//                users.document(uid).update(fpMap);
//                tsLong++;
//            }
//        });
//        users.document("04mMjzaqIeV1IbJSmbwUuGVOc0B2").get().addOnCompleteListener(task -> {
//            if (task.getResult().exists()) {
//                Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put(Consts.MUST_INFO, "false");
//                users.document("04mMjzaqIeV1IbJSmbwUuGVOc0B2").update(fpMap);
//            } else Log.d("DialogDebug", "Task doesn't exist!!!");
//        });

//        users.get().addOnCompleteListener(task -> {
//            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                String image = snapshot.getString(Consts.IMAGE);
//                String country = snapshot.getString(Consts.COUNTRY);
//                String gender = snapshot.getString(Consts.GENDER);
//                String uid = snapshot.getId();
//
//                if (snapshot.getString(Consts.IMAGE).equals(Consts.DEFAULT)) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("must_info", "false");
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//
//                if (snapshot.getString(Consts.COUNTRY).equals("Russia") && snapshot.getString(Consts.GENDER).equals("Male")) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("rating", 2);
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//
//                if (snapshot.getString(Consts.COUNTRY).equals("Ukraine") && snapshot.getString(Consts.GENDER).equals("Male")) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("rating", 2);
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//
//                if (snapshot.getString(Consts.COUNTRY).equals("Kazakhstan") && snapshot.getString(Consts.GENDER).equals("Male")) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("rating", 2);
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//
//                if (snapshot.getString(Consts.COUNTRY).equals("Belarus") && snapshot.getString(Consts.GENDER).equals("Male")) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("rating", 2);
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//
//                if (snapshot.getString(Consts.COUNTRY).equals("Uzbekistan") && snapshot.getString(Consts.GENDER).equals("Male")) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("rating", 2);
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//
//                if (snapshot.getString(Consts.COUNTRY).equals("Kyrgyzstan") && snapshot.getString(Consts.GENDER).equals("Male")) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("rating", 2);
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//
//                if (snapshot.getString(Consts.COUNTRY).equals("Afghanistan")) {
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put("rating", 1);
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//            }
//        });
//        users.get().addOnCompleteListener(task -> {
//            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
//                if (!snapshot.getString(Consts.IMAGE).equals(Consts.DEFAULT) &&
//                        !snapshot.getString(Consts.AGE).equals(Consts.DEFAULT) &&
//                        !snapshot.getString(Consts.COUNTRY).equals(Consts.DEFAULT)) {
//                    String uid = snapshot.getId();
//                    Map<String, Object> fpMap = new HashMap<>();
//                    fpMap.put(MUST_INFO_ACH, "true");
//                    users.document(uid).update(fpMap);
//                    //new RatingRepository().addAchievement(FULL_PROFILE_ACH);
//                }
//            }
//        });
       /* realtimeReference.child(Consts.USERS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    Log.d("FpDebug", "User uid is " + uid);
                    new RatingRepository().isAchExist(uid, FULL_PROFILE_ACH, flag -> {
                        if (flag) {
                            //Log.d("FpDebug", "User with full profile uid is " + uid);
                            Map<String, Object> fpMap = new HashMap<>();
                            fpMap.put(Consts.RATING, 14);
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
                boolean value = !image.equals(Consts.DEFAULT) && !age.equals(Consts.DEFAULT) && !country.equals(Consts.DEFAULT);
                callback.setBool(value);
            }
        });
    }


    public void setDialogLastOpenDate() {
        mPrefs.setFPOpenDate();
    }

    public String getGender() {
        String s = mPrefs.getGender();
        return mPrefs.getGender();
    }

    public String getSearchGender() {
        return mPrefs.getGenderSearch();
    }

    public void setFPDialogLastOpenDate() {
        mPrefs.setFPOpenDate();
    }

    private boolean isOneDayGone(String key) {
        float days = 0f;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date1 = dateFormat.parse(mPrefs.getValue(key));
            Date date2 = Calendar.getInstance().getTime();
            long diff = date2.getTime() - date1.getTime();
            days = (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mPrefs.getValue(key).equals(Consts.DEFAULT) || (days >= 1 && !mPrefs.getValue(key).equals(""));
    }

    public void updateToken() {
        Map<String, Object> ntMap = new HashMap<>();
        ntMap.put(Consts.DEVICE_TOKEN, getDeviceToken());
        users.document(getUid()).update(ntMap);
        realtimeReference.child(Consts.USERS_DB).child(getUid()).updateChildren(ntMap);
    }


    public void setFirstOpenDate() {
        if (mPrefs.getFirstOpenDate().equals(Consts.DEFAULT)) {
            mPrefs.setFirstOpenDate();
        }
    }

    public boolean isGenderDefault() {
        return mPrefs.getGenderSearch().equals(Consts.DEFAULT);
    }

    public void setGender(String gender) {
        if (!gender.equals(Consts.DEFAULT)) {
            mPrefs.setGenderSearch(gender);
        }
    }

    public void hasFullProfileInfo(BoolCallback callback) {
        new RatingRepository().isAchievementExist(FULL_PROFILE_ACH, flag -> {
            if (flag) {
                mPrefs.clearValue(Consts.FP_OPEN_DATE);
                callback.setBool(false);
            } else callback.setBool(isOneDayGone(Consts.FP_OPEN_DATE));
        });
    }

    public void hasDefaultMustInfo(BoolCallback callback) {
        Log.d("DialogDebug", "Uid is " + getUid());
        users.document(getUid()).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                Log.d("DialogDebug", "Task exists");
                DocumentSnapshot snapshot = task.getResult();
                boolean flag = snapshot.getString(Consts.IMAGE).equals(Consts.DEFAULT) ||
                        snapshot.getString(Consts.AGE).equals(Consts.DEFAULT) ||
                        snapshot.getString(Consts.COUNTRY).equals(Consts.DEFAULT);
                callback.setBool(flag);
            } else Log.d("DialogDebug", "Task doesn't exist!!!");
        });
    }

    // check if the value of the given key is default or not
    private boolean notDefault(String[] keys, DocumentSnapshot snapshot) {
        boolean result = true;
        for (String key : keys) {
            if (snapshot.getString(key).equals(Consts.DEFAULT)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void hasNecessaryInfo(BoolCallback callback) {
        String[] keys = {Consts.GENDER, Consts.AGE, Consts.IMAGE, Consts.COUNTRY};
        users.document(getUid()).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                DocumentSnapshot snapshot = task.getResult();
                new RatingRepository().isAchievementExist(FULL_PROFILE_ACH, flag -> {
                    if (flag) {
                        mPrefs.clearValue(Consts.DIALOG_OPEN_DATE);
                        callback.setBool(false);
                    } else
                        callback.setBool(isOneDayGone(Consts.DIALOG_OPEN_DATE) && notDefault(keys, snapshot));
                });
            }
        });
    }

    public void setFullProfile() {
        Map<String, Object> fpMap = new HashMap<>();
        fpMap.put(FULL_PROFILE_ACH, "true");
        users.document(getUid()).update(fpMap);
    }

    public boolean isUserExist() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser != null;
    }

    public void getAllCurrentUserInfo(final UserCallback callback) {
        if (isUserExist()) {
            users.document(getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                callback.setUser(snapshot.toObject(FsUser.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(Consts.TAG_INFO, "getAllCurrentUserInfo failure");
                }
            });
        }
    }

    public void getTransformedActions(ActionItemCallback callback) {
        List<ActionItem> actionItems = new ArrayList<>();
        if (actionItems.size() == 0) {
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
                    actionItems.clear();
                });
            });
        }
    }

    public void getActionsCountInfo(ActionsCountInfoCallback callback) {
//        updateUsersLikesVisits();
        getActionCount("Visits", visitsCount ->
                getActionCount("Likes", likesCount -> callback.setActions(visitsCount, likesCount)));
    }

    /**
     * Method to update database with likes/visits for all users (only for development purpose)
     */
    private void updateUsersLikesVisits() {
        //Идем по всем пользователям
        realtimeReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Взяли первого пользователя
                    if (snapshot.child("uid").exists()) {
                        String uidUpdate = snapshot.child("uid").getValue().toString();
                        //Если у него нет поля visits надо его создать
                        if (!snapshot.child("visits").exists()) {
                            //Делаем запрос в таблицу Visits и ищем там текущего пользователя
                            realtimeReference.child("Visits").child(uidUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long total = 0;
                                    //Если есть какие то записи то total равен этим записям
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        total = dataSnapshot.getChildrenCount();
                                    }
                                    //Если нет то total ставим 0
                                    String stringLikesAmount = Long.toString(total);
                                    //Записываем в поле visits у пользователя либо 0 либо значение
                                    realtimeReference.child("Users").child(uidUpdate).child("visits").setValue(stringLikesAmount);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //Если ошибка все равно в поле visits пользователю пишем значение 9
                                    realtimeReference.child("Users").child(uidUpdate).child("visits").setValue("0");
                                }
                            });
                        } else {
                            Log.d("test", "visits exist");
                        }


                        if (!snapshot.child("likes").exists()) {
                            //Делаем запрос в таблицу Visits и ищем там текущего пользователя
                            realtimeReference.child("Likes").child(uidUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long total = 0;
                                    //Если есть какие то записи то total равен этим записям
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        total = dataSnapshot.getChildrenCount();
                                    }
                                    //Если нет то total ставим 0
                                    String stringLikesAmount = Long.toString(total);
                                    //Записываем в поле visits у пользователя либо 0 либо значение
                                    realtimeReference.child("Users").child(uidUpdate).child("likes").setValue(stringLikesAmount);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //Если ошибка все равно в поле visits пользователю пишем значение 9
                                    realtimeReference.child("Users").child(uidUpdate).child("likes").setValue("0");
                                }
                            });
                        } else {
                            Log.d("test", "likes exist");

                        }

                    } else {
                        //Если у пользователя нет поля uid
                        String uidKeyUpdate = snapshot.getKey();
                        realtimeReference.child("Users").child(uidKeyUpdate).child("uid").setValue(uidKeyUpdate);

//                        String uidUpdate = snapshot.child("uid").getValue().toString();
                        //Если у него нет поля visits надо его создать
                        if (!snapshot.child("visits").exists()) {
                            //Делаем запрос в таблицу Visits и ищем там текущего пользователя
                            realtimeReference.child("Visits").child(uidKeyUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long total = 0;
                                    //Если есть какие то записи то total равен этим записям
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        total = dataSnapshot.getChildrenCount();
                                    }
                                    //Если нет то total ставим 0
                                    String stringLikesAmount = Long.toString(total);
                                    //Записываем в поле visits у пользователя либо 0 либо значение
                                    realtimeReference.child("Users").child(uidKeyUpdate).child("visits").setValue(stringLikesAmount);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //Если ошибка все равно в поле visits пользователю пишем значение 9
                                    realtimeReference.child("Users").child(uidKeyUpdate).child("visits").setValue("0");
                                }
                            });
                        } else {
                            Log.d("test", "visits exist");
                        }


                        if (!snapshot.child("likes").exists()) {
                            //Делаем запрос в таблицу Visits и ищем там текущего пользователя
                            realtimeReference.child("Likes").child(uidKeyUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long total = 0;
                                    //Если есть какие то записи то total равен этим записям
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        total = dataSnapshot.getChildrenCount();
                                    }
                                    //Если нет то total ставим 0
                                    String stringLikesAmount = Long.toString(total);
                                    //Записываем в поле visits у пользователя либо 0 либо значение
                                    realtimeReference.child("Users").child(uidKeyUpdate).child("likes").setValue(stringLikesAmount);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //Если ошибка все равно в поле visits пользователю пишем значение 9
                                    realtimeReference.child("Users").child(uidKeyUpdate).child("likes").setValue("0");
                                }
                            });
                        } else {
                            Log.d("test", "likes exist");

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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

    public void makeDialogOpenDateDefault() {
        mPrefs.setDialogOpenDate(Consts.DEFAULT);
    }

    public void clearDialogOpenDate() {
        mPrefs.clearValue(Consts.DIALOG_OPEN_DATE);
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
        realtimeReference.child("Users").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Consts.VISITS).exists() && dataSnapshot.child(Consts.LIKES).exists()) {
                    EventBus.getDefault().post(new VisitsEvent(Long.valueOf(dataSnapshot.child(Consts.VISITS).getValue().toString()), Long.valueOf(dataSnapshot.child(Consts.LIKES).getValue().toString())));
                } else {
                    Log.d("error", "something wrong");
                }

//                callback.setActionCount(Long.valueOf(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("error", "something wrong" + databaseError.getMessage());

            }
        });

    }


    public String getUserGender() {
        return mPrefs.getGender();
    }

    public void getSecondaryInfoDialog() {
        users.document(getUid()).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                DocumentSnapshot snapshot = task.getResult();
                String image = snapshot.getString(Consts.IMAGE);
                String age = snapshot.getString(Consts.AGE);
                String country = snapshot.getString(Consts.COUNTRY);
                boolean mustInfo = !image.equals(Consts.DEFAULT) && !age.equals(Consts.DEFAULT) && !country.equals(Consts.DEFAULT);
                EventBus.getDefault().post(new BooleanEvent(!image.equals(Consts.DEFAULT) && !age.equals(Consts.DEFAULT) && !country.equals(Consts.DEFAULT)));
            }
        });


        new RatingRepository().isAchievementExist(FULL_PROFILE_ACH, flag -> {
//            if (flag) {
//                mPrefs.clearValue(Consts.FP_OPEN_DATE);
//                callback.setBool(false);
//            } else callback.setBool(isOneDayGone(Consts.FP_OPEN_DATE));
        });
    }


    /**
     * Searching user by id in Firestore
     *
     * @param idSoc
     */
    public void searchUser(String idSoc) {
        users.whereEqualTo(Consts.ID_SOC, idSoc)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FsUser fsUser = document.toObject(FsUser.class);
                                    EventBus.getDefault().post(new SearchEvent(Consts.SEARCH_EVENT_OK, fsUser.getUid()));
                                }
                            } else {
                                EventBus.getDefault().post(new SearchEvent(Consts.SEARCH_EVENT_FAILURE, "User Doesn't Exist"));
                            }
                        } else {
                            EventBus.getDefault().post(new SearchEvent(Consts.SEARCH_EVENT_FAILURE, "User Doesn't Exist"));
                        }
                    }
                });
    }
}
