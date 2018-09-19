package com.borisruzanov.russianwives.zTEST;

import com.borisruzanov.russianwives.base.BaseActivity;

public class ChatUdacityActivity extends BaseActivity{
//
//    private static final String TAG = " ==> Chat Activity ==>";
//    Toolbar toolbar;
//
//
//    public static final String ANONYMOUS = "anonymous";
//    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
//    public static final String FRIENDLY_MSG_LENGTH_KEY = "friendly_msg_length";
//    public static final int RC_SIGN_IN = 1;
//    private static final int RC_PHOTO_PICKER =  2;
//
//    private ListView mMessageListView;
//    private MessageUdacityAdapter mMessageUdacityAdapter;
//    private ProgressBar mProgressBar;
//    private ImageButton mPhotoPickerButton;
//    private EditText mMessageEditText;
//    private Button mSendButton;
//
//    private String mUsername;
//
//    //Firebase
//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference mMessageDatabaseReference;
//    private ChildEventListener mChildEventListener;
//    private FirebaseAuth mFirebaseAuth;
//    private FirebaseAuth.AuthStateListener mAuthstateListener;
//    private FirebaseStorage mFirebaseStorage;
//    private StorageReference mChatPhotosStorageReference;
//    private FirebaseRemoteConfig mFirebaseRemoteConfig;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat_udacity);
//
//        mUsername = ANONYMOUS;
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseStorage = FirebaseStorage.getInstance();
//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//
//        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("messages");
//        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");
//
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mMessageListView = (ListView) findViewById(R.id.messageListView);
//        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
//        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
//        mSendButton = (Button) findViewById(R.id.sendButton);
//
//
//        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
//        mMessageUdacityAdapter = new MessageUdacityAdapter(this, R.layout.item_message, friendlyMessages);
//        mMessageListView.setAdapter(mMessageUdacityAdapter);
//
//        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
//
//        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TODO -PhotoPicker- вынести отдельно
//                Intent intent =  new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent,"Complete action using"), RC_PHOTO_PICKER) ;
//            }
//        });
//
//        mMessageEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
////
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            if (charSequence.toString().trim().length() > 0) {
//                mSendButton.setEnabled(true);
//            } else {
//                mSendButton.setEnabled(false);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//        }
//    });
//
//        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
//
//        mSendButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null);
//            mMessageDatabaseReference.push().setValue(friendlyMessage);
//
//            mMessageEditText.setText("");
//        }
//    });
//
//        mAuthstateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    onSignedInInitialized(user.getDisplayName());
//                } else {
//                    onSignedOutCleanup();
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setAvailableProviders(Arrays.asList(
//                                            new AuthUI.IdpConfig.EmailBuilder().build(),
//                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
//                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
//                                    .build(),
//                            RC_SIGN_IN);
//                }
//            }
//        };
//
//
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setDeveloperModeEnabled(BuildConfig.DEBUG)
//                .build();
//        mFirebaseRemoteConfig.setConfigSettings(configSettings);
//        Map<String, Object> defaultConfigMap = new HashMap<>();
//        defaultConfigMap.put(FRIENDLY_MSG_LENGTH_KEY, DEFAULT_MSG_LENGTH_LIMIT);
//        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);
//        fetchConfig();
//
//    }
//
//    public void fetchConfig() {
//        long cacheExpiration = 3600;
//        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
//            cacheExpiration = 0;
//        }
//        mFirebaseRemoteConfig.fetch(cacheExpiration)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        mFirebaseRemoteConfig.activateFetched();
//                        applyRetrievedLengthLimit();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        applyRetrievedLengthLimit();
//                    }
//                });
//    }
//
//    private void applyRetrievedLengthLimit() {
//        Long friendly_message_length = mFirebaseRemoteConfig.getLong(FRIENDLY_MSG_LENGTH_KEY);
//        mMessageEditText.setFilters(new InputFilter[]{
//                new InputFilter.LengthFilter(friendly_message_length.intValue())
//        });
//        Log.d(TAG, FRIENDLY_MSG_LENGTH_KEY + " = " + friendly_message_length);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            if (resultCode == RESULT_OK) {
//
//            } else if (resultCode == RESULT_CANCELED) {
//                finish();
//            } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
//                Uri selectedImageUri = data.getData();
//                StorageReference photoRef =
//                        mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
//                photoRef.putFile(selectedImageUri).addOnSuccessListener(this,
//                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                                FriendlyMessage friendlyMessage =
//                                        new FriendlyMessage(null, mUsername, downloadUrl.toString());
//                                mMessageDatabaseReference.push().setValue(friendlyMessage);
//                            }
//                        });
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.sign_out_menu:
//                AuthUI.getInstance().signOut(this);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mAuthstateListener != null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthstateListener);
//        }
//        detachDatabaseListener();
//        mMessageUdacityAdapter.clear();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mFirebaseAuth.addAuthStateListener(mAuthstateListener);
//    }
//
//
//        private void onSignedInInitialized(String username) {
//        mUsername = username;
//        attachDatabaseListener();
//
//    }
//
//    private void onSignedOutCleanup() {
//        mUsername = ANONYMOUS;
//        mMessageUdacityAdapter.clear();
//        detachDatabaseListener();
//    }
//
//    private void attachDatabaseListener() {
//        if (mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    FriendlyMessage friendlyMessage = dataSnapshot.setValue(FriendlyMessage.class);
//                    mMessageUdacityAdapter.add(friendlyMessage);
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//            mMessageDatabaseReference.addChildEventListener(mChildEventListener);
//        }
//    }
//
//    private void detachDatabaseListener() {
//        if (mChildEventListener != null) {
//            mMessageDatabaseReference.removeEventListener(mChildEventListener);
//            mChildEventListener = null;
//        } else {
//        }
//    }


}
