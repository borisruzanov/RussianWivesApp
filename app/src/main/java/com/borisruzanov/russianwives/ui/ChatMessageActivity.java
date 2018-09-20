package com.borisruzanov.russianwives.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.Adapters.MessageAdapter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.Message;
import com.borisruzanov.russianwives.mvp.model.interactor.ChatMessageInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.ChatMessagePresenter;
import com.borisruzanov.russianwives.mvp.view.ChatMessageView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageActivity extends MvpAppCompatActivity implements ChatMessageView {

    //UI
    private String mChatUser;
    private Toolbar mChatToolbar;
    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;
    private SwipeRefreshLayout mRefreshLayout;


    //MVP
    @InjectPresenter
    ChatMessagePresenter presenter;

    @ProvidePresenter
    public ChatMessagePresenter provideChatMessagePresenter() {
        return new ChatMessagePresenter(new ChatMessageInteractor(new FirebaseRepository()));
    }

    //Utility
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    //List
    private RecyclerView mMessagesList;
    private final List<Message> messageList = new ArrayList<>();
    private MessageAdapter mAdapter;
    private LinearLayoutManager mLinearLayout;

    //Pagination
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";


    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mChatUser = getIntent().getStringExtra("uid");

        presenter.initChat(mChatUser);

        //UI
        mChatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        //Custom Action bar Items
        mTitleView = findViewById(R.id.custom_bar_title);
        mTitleView.setText(getIntent().getStringExtra("nameText"));
        mProfileImage = findViewById(R.id.custom_bar_image);
        Glide.with(this).load(getIntent().getStringExtra("photo_url")).into(mProfileImage);


        //List of Message
        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);
        mLinearLayout = new LinearLayoutManager(this);
        mAdapter = new MessageAdapter(getIntent().getStringExtra("photo_url"), getIntent().getStringExtra("nameText"));

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        mMessagesList.setAdapter(mAdapter);
        //TODO Rename Item layout from message_single_layout
        //TODO Rename all variables


        //Buttons
        mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);
        mChatAddBtn.setOnClickListener(v -> addFile());
        mChatSendBtn = (ImageButton) findViewById(R.id.chat_send_btn);
        mChatMessageView = (EditText) findViewById(R.id.chat_message_view);

        mChatSendBtn.setOnClickListener(v -> {
            presenter.sendMessage(mChatUser, mChatMessageView.getText().toString());
        });

        //Pagination
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);

        //Utility
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mLastSeenView = (TextView) findViewById(R.id.custom_bar_seen);

        //Loading messages first time
        loadMessages();
        for (Message message : messageList) {
            Log.d("ccc", "Message - " + message.getMessage() + "from - " + message.getFrom() +
                    "timestamp - " + message.getTime());
        }

        mRefreshLayout.setOnRefreshListener(() -> {
            mCurrentPage++;
            itemPos = 0;
            loadMoreMessages();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                //handle the home button onClick event here.
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setEmptyMessage() {
        mChatMessageView.setText("");
    }

    private void addFile() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            presenter.sendImage(mChatUser, data.getData());
        }
    }

    /**
     * Loading messages after swiping 2+ times
     */
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                String messageKey = dataSnapshot.getKey();
                if (!mPrevKey.equals(messageKey)) {
                    messageList.add(itemPos++, message);
                } else {
                    mPrevKey = mLastKey;
                }
                if (itemPos == 1) {
                    mLastKey = messageKey;
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                mLinearLayout.scrollToPosition(itemPos);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Load messages only once
     */
    private void loadMessages() {
        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                Log.d("ccc", "TIMESTAMP----------------------> is " + message.getType());
                itemPos++;
                if (itemPos == 1) {
                    String messageKey = dataSnapshot.getKey();
                    mLastKey = messageKey;
                    mPrevKey = messageKey;
                }
                messageList.add(message);
                Log.d("ccc", "in Activity timestamp " + String.valueOf(message.getTime()));
                mAdapter.setData(messageList);
                mMessagesList.scrollToPosition(messageList.size() - 1);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
