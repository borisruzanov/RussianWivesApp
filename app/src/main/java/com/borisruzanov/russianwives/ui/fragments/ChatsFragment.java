package com.borisruzanov.russianwives.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.borisruzanov.russianwives.Adapters.ChatsAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserChat;
import com.borisruzanov.russianwives.mvp.presenter.ChatsPresenter;
import com.borisruzanov.russianwives.mvp.view.ChatsView;
import com.borisruzanov.russianwives.ui.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatsFragment extends MvpAppCompatFragment implements ChatsView {

    //TODO Refactor to Activities Fragment

    RecyclerView recyclerChatsList;
    ChatsAdapter chatsAdapter;

    private View mMainView;

    @InjectPresenter
    ChatsPresenter chatsPresenter;

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_friends, container, false);
        Log.d(Contract.TAG, "INSIDEE CHATS");


//        ArrayList<FsUser> listUsers = (ArrayList<FsUser>) getArguments().getSerializable("ingredients");
//        chatsList.addAll(listUsers);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid());

        Log.d("onlineStatus uid", "mUserDatabase " + FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid()));


        recyclerChatsList = (RecyclerView) mMainView.findViewById(R.id.friends_fragment_recycler_chats);
        recyclerChatsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerChatsList.setHasFixedSize(true);
        chatsAdapter = new ChatsAdapter(onItemClickCallback, mUserDatabase, mAuth);
        recyclerChatsList.setAdapter(chatsAdapter);

        chatsPresenter.getUserChatList();


        /**
         * Chats List
         */

//
//        if (userChats.isEmpty()){
//            Log.d(Contract.TAG, "CHATSList is empty");
//        }else {
//            Log.d(Contract.TAG, "ChatsFragment - UserChat model name is  " + userChats.get(0).getName());
//            Log.d(Contract.TAG, "ChatsFragment - UserChat model seen is  " + userChats.get(0).getSeen());
//            Log.d(Contract.TAG, "ChatsFragment - UserChat model timestamp is  " + userChats.get(0).getTimestamp());
//        }


//        mConvDatabase.keepSynced(true);
//        Query conversationQuery = mConvDatabase.orderByChild("timestamp");
//        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
//        mUsersDatabase.keepSynced(true);
//        final String list_user_id = getRef(i).getKey();
//        Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);


//        for (FsUser user : chatsList) {
//            Log.d(Contract.TAG, "chatsList FsUser name " + user.getName());
//        }

//
//        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
//        mAuth = FirebaseAuth.getInstance();
//
//        mCurrent_user_id = mAuth.getCurrentUser().getOnline();
//
//        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
//        mFriendsDatabase.keepSynced(true);
//        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("FsUser");
//        mUsersDatabase.keepSynced(true);
//
//
//        mFriendsList.setHasFixedSize(true);
//        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment






        return mMainView;
    }

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = (view, position) -> {
        Log.d(Contract.TAG, "In onCLICK");
        chatsPresenter.openChat(position);



//        mChatUser = userChats.get(position).getUserId();
//
//        Map chatAddMap = new HashMap();
//        chatAddMap.put("activity", "visit");
//        chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
//        chatAddMap.put("uid", mCurrent_user_id);
//        DatabaseReference mDatabase = mRootRef.child("Activity").child(mChatUser).push();
//        mDatabase.updateChildren(chatAddMap);

//
//        mRootRef.child("Activity").child(mChatUser).child(mCurrent_user_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
////                    Map chatUserMap = new HashMap();
////                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + mChatUser, chatAddMap);
////                    chatUserMap.put("Chat/" + mChatUser + "/" + mCurrentUserId, chatAddMap);
//
////                    mRootRef.ad(chatAddMap, new DatabaseReference.CompletionListener() {
////                        @Override
////                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
////                            if (databaseError != null) {
////                                Log.d(Contract.TAG, "initializeChat Error is " + databaseError.getMessage().toString());
////                            }
////                        }
////                    });
////                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        searchPresenter.openFriend(position);
       /* Intent friendActivityIntent = new Intent(getContext(), FriendActivity.class);
                FsUser itemClicked = fsUserList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("image", itemClicked.getImage());
                bundle.putString("name", itemClicked.getName());
                bundle.putParcelableArrayList("ingredients", new ArrayList<Parcelable>(itemClicked.getIngredients()));
                bundle.putParcelableArrayList("steps", new ArrayList<Parcelable>(itemClicked.getSteps()));
                bundle.putInt("swrvings", itemClicked.getServings());

                if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                    Log.d(TAG_WORK_CHECKING, "It is more than 600 dp");
                    Intent dataIntent = new Intent(getContext(), TabletActivity.class);
                    dataIntent.putExtras(bundle);
                    startActivity(dataIntent);

                } else {

                    DetailedFragment detailedFragment = new DetailedFragment();
                    detailedFragment.setArguments(bundle);

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.main_frame_list, detailedFragment);
                    transaction.commit();
                }        startActivity(friendActivityIntent);*/
    };

    @Override
    public void showUserChats(List<UserChat> userChats) {
        recyclerChatsList.post(() -> chatsAdapter.setData(userChats));
    }

    @Override
    public void openChat(String uid, String name, String image) {
        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
        chatIntent.putExtra("uid", uid);
        chatIntent.putExtra("name",name);
        chatIntent.putExtra("photo_url", image);
        startActivity(chatIntent);
    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
//
//                Friends.class,
//                R.layout.users_single_layout,
//                FriendsViewHolder.class,
//                mFriendsDatabase
//
//
//        ) {
//            @Override
//            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, Friends friends, int i) {
//
//                friendsViewHolder.setDate(friends.getDate());
//
//                final String list_user_id = getRef(i).getKey();
//
//                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        final String userName = dataSnapshot.child("name").getValue().toString();
//                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
//
//                        if(dataSnapshot.hasChild("online")) {
//
//                            String userOnline = dataSnapshot.child("online").getValue().toString();
//                            friendsViewHolder.setUserOnline(userOnline);
//
//                        }
//
//                        friendsViewHolder.setName(userName);
//                        friendsViewHolder.setUserImage(userThumb, getContext());
//
//                        friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};
//
//                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//                                builder.setTitle("Select Options");
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        //Click Event for each item.
//                                        if(i == 0){
//
//                                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
//                                            profileIntent.putExtra("user_id", list_user_id);
//                                            startActivity(profileIntent);
//
//                                        }
//
//                                        if(i == 1){
//
//                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
//                                            chatIntent.putExtra("user_id", list_user_id);
//                                            chatIntent.putExtra("user_name", userName);
//                                            startActivity(chatIntent);
//
//                                        }
//
//                                    }
//                                });
//
//                                builder.show();
//
//                            }
//                        });
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        };
//
//        mFriendsList.setAdapter(friendsRecyclerViewAdapter);
//
//
//    }
//
//
//    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
//
//        View mView;
//
//        public FriendsViewHolder(View itemView) {
//            super(itemView);
//
//            mView = itemView;
//
//        }
//
//        public void setDate(String date){
//
//            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
//            userStatusView.setText(date);
//
//        }
//
//        public void setName(String name){
//
//            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
//            userNameView.setText(name);
//
//        }
//
//        public void setUserImage(String thumb_image, Context ctx){
//
//            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
//            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);
//
//        }
//
//        public void setUserOnline(String online_status) {
//
//            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);
//
//            if(online_status.equals("true")){
//
//                userOnlineView.setVisibility(View.VISIBLE);
//
//            } else {
//
//                userOnlineView.setVisibility(View.INVISIBLE);
//
//            }
//
//        }
//
//
//    }


}
