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
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Chat;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.models.UserChat;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    //TODO Refactor to Activities Fragment

    RecyclerView recyclerChatsList;
    ChatsAdapter chatsAdapter;
    List<UserChat> chatsList = new ArrayList<>();

    private View mMainView;

    //Firebase
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private String mCurrent_user_id;

    List<UserChat> userChats = new ArrayList<>();


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_friends, container, false);
        Log.d(Contract.TAG, "INSIDEE CHATS");



//        ArrayList<User> listUsers = (ArrayList<User>) getArguments().getSerializable("ingredients");
//        chatsList.addAll(listUsers);


        recyclerChatsList = (RecyclerView) mMainView.findViewById(R.id.friends_fragment_recycler_chats);
        recyclerChatsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerChatsList.setHasFixedSize(true);
        chatsAdapter = new ChatsAdapter(onItemClickCallback);
        recyclerChatsList.setAdapter(chatsAdapter);

        //TODO REFACTOR Chats Fragment with repository
        mCurrent_user_id = new FirebaseRepository().getUid();
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConvDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Chat> chatList = new ArrayList<>();
                List<String> uidList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(Contract.TAG, "ChatsFragment - chat Request - Uid" + snapshot.getKey() + " ");
                    //This is list of chats UID's
                    uidList.add(snapshot.getKey());
                    long timeStamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());
                    boolean seen = Boolean.getBoolean(snapshot.child("seen").toString());
                    Log.d(Contract.TAG, "ChatsFragment - chat Request - Object - " + "timestamp is " + timeStamp + "message seen is " + seen);
                    //This is list of Chats Objects
                    chatList.add(new Chat(timeStamp, seen));
                }

                new FirebaseRepository().getNeededUsers(uidList, userList -> {
                    for(User user: userList){
                        Log.d(Contract.TAG, "Полученное имя из репозитория " + user.getName());
                    }

                    if(userList.isEmpty()) Log.d(Contract.TAG, "LIST IS EMPTY!!!");


                    for (int i = 0; i < userList.size(); i++) {
                        String name = userList.get(i).getName();
                        String image = userList.get(i).getImage();
                        long timeStamp = chatList.get(i).getTimeStamp();
                        boolean seen = chatList.get(i).getSeen();
                        userChats.add(new UserChat(name, image, timeStamp, seen));

                        Log.d(Contract.TAG, "Отображаем имя в UI фрагмента  " + userChats.get(i).getName());
                        Log.d(Contract.TAG, "Отображаем видимость в UI фрагмента  " + userChats.get(i).getSeen());
                        Log.d(Contract.TAG, "Отображаем таймстэмп в UI фрагмента  " + userChats.get(i).getTimestamp());

                        chatsAdapter.setData(userChats);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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


//        for (User user : chatsList) {
//            Log.d(Contract.TAG, "chatsList User name " + user.getName());
//        }

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

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = (view, position) -> {
        Log.d(Contract.TAG, "In onCLICK");
//        searchPresenter.openFriend(position);
       /* Intent friendActivityIntent = new Intent(getContext(), FriendActivity.class);
                User itemClicked = userList.get(position);
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
