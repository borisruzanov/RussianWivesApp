package com.borisruzanov.russianwives.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.borisruzanov.russianwives.Adapters.RequestsAdapter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FriendsFragment extends Fragment {

    //TODO Refactor to Activities Fragment

    RecyclerView recyclerRequests;
    RequestsAdapter requestsAdapter;
    List<User> requestsList = new ArrayList<>();

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_friends, container, false);

        /**
         * Requests List
         */
        recyclerRequests = (RecyclerView) mMainView.findViewById(R.id.friends_fragment_recycler_requests) ;
        recyclerRequests.setLayoutManager(new LinearLayoutManager(getActivity()));

//        ArrayList<User> listUsers = (ArrayList<User>) getArguments().getSerializable("ingredients");
//        requestsList.addAll(listUsers);


        new FirebaseRepository().getUserDataTierTwo("FriendsLogic", "hereUserUid",
                "Requests", stringList -> new FirebaseRepository().getNeededUsers(stringList, userList -> {
                    for (String uid : stringList) Log.d(Contract.TAG, "Uid " + uid);
                    for (User user : userList) {
                        Log.d(Contract.TAG, "User name " + user.getName());
                        List<User> listIngredients = userList;

                        requestsList.addAll(listIngredients);
                        requestsAdapter = new RequestsAdapter(requestsList);
                        recyclerRequests.setAdapter(requestsAdapter);
                    }
                }));
        for (User user : requestsList) {
            Log.d(Contract.TAG, "requestsList User name " + user.getName());
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
