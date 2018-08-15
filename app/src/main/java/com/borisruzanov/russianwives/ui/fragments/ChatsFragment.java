package com.borisruzanov.russianwives.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.borisruzanov.russianwives.Adapters.ChatsAdapter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    //TODO Refactor to Activities Fragment

    RecyclerView mConvList;
    ChatsAdapter chatsAdapter;
    List<User> chatsList = new ArrayList<>();

    private View mMainView;

    //Firebase
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private String mCurrent_user_id;




    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_friends, container, false);

        /**
         * Requests List
         */
        mConvList = (RecyclerView) mMainView.findViewById(R.id.friends_fragment_recycler_chats) ;
        mConvList.setLayoutManager(new LinearLayoutManager(getActivity()));

//        ArrayList<User> listUsers = (ArrayList<User>) getArguments().getSerializable("ingredients");
//        chatsList.addAll(listUsers);


        //TODO REFACTOR Chats Fragment with repository
        //Firebase
        mCurrent_user_id = new FirebaseRepository().getUid();
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (User user : userList) {
                    Log.d(Contract.TAG, "User name " + user.getName());
                    List<User> listIngredients = userList;

                    chatsList.addAll(listIngredients);
                    chatsAdapter = new ChatsAdapter(chatsList);
                    mConvList.setAdapter(chatsAdapter);
                }            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mConvDatabase.keepSynced(true);
        Query conversationQuery = mConvDatabase.orderByChild("timestamp");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);
        final String list_user_id = getRef(i).getKey();
        Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);



        new FirebaseRepository().getUserDataTierTwo("FriendsLogic", "hereUserUid",
                "Requests", stringList -> new FirebaseRepository().getNeededUsers(stringList, userList -> {
                    for (String uid : stringList) Log.d(Contract.TAG, "Uid " + uid);
                    for (User user : userList) {
                        Log.d(Contract.TAG, "User name " + user.getName());
                        List<User> listIngredients = userList;

                        chatsList.addAll(listIngredients);
                        chatsAdapter = new ChatsAdapter(chatsList);
                        mConvList.setAdapter(chatsAdapter);
                    }
                }));
        for (User user : chatsList) {
            Log.d(Contract.TAG, "chatsList User name " + user.getName());
        }

//
//        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
//        mAuth = FirebaseAuth.getInstance();
//
//        mCurrent_user_id = mAuth.getCurrentUser().getUid();
//
//        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
//        mFriendsDatabase.keepSynced(true);
//        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("User");
//        mUsersDatabase.keepSynced(true);
//
//
//        mFriendsList.setHasFixedSize(true);
//        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
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
