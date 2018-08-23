package com.borisruzanov.russianwives.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.borisruzanov.russianwives.Adapters.ActivitiesAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Activities;
import com.borisruzanov.russianwives.models.ActivitiesListItem;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesFragment extends Fragment {


    RecyclerView recyclerChatsList;
    ActivitiesAdapter activitiesAdapter;

    List<ActivitiesListItem> activitiesListItems = new ArrayList<>();

    private DatabaseReference mConvDatabase;
    private String mCurrent_user_id;

    //TODO Implement Chats fragment functionality
    private View mMainView;


    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_activities, container, false);

        recyclerChatsList = (RecyclerView) mMainView.findViewById(R.id.friends_fragment_recycler_activities);
        recyclerChatsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerChatsList.setHasFixedSize(true);
        activitiesAdapter  = new ActivitiesAdapter(onItemClickCallback);
        recyclerChatsList.setAdapter(activitiesAdapter);



        mCurrent_user_id = new FirebaseRepository().getUid();
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Activity").child(mCurrent_user_id);
        mConvDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Activities> activitiesList = new ArrayList<>();
                List<String> uidList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //This is list of chats UID's
                    long timeStamp = Long.valueOf(snapshot.child("timestamp").getValue().toString());
                    String uid = snapshot.child("uid").getValue().toString();
                    String activity = snapshot.child("activity").getValue().toString();
                    Log.d("vv", "Values " + uid + " " + timeStamp + " " + activity);
                    //This is list of Chats Objects
                    activitiesList.add(new Activities(activity, uid, timeStamp));
                    uidList.add(uid);
                }
                new FirebaseRepository().getNeededUsers(uidList, userList -> {

                    for (int i = 0; i < userList.size(); i++) {
                        Log.d("vv", "Values "
                                + activitiesList.get(i).getActivity() + " "
                                + activitiesList.get(i).getTimeStamp() + " "
                                + userList.get(i).getImage() + " "
                                + userList.get(i).getName());
                        String name = userList.get(i).getName();
                        String image = userList.get(i).getImage();
                        String activity  = activitiesList.get(i).getActivity();
                        long timeStamp = activitiesList.get(i).getTimeStamp();

                        activitiesListItems.add(new ActivitiesListItem(name, activity, timeStamp,image));
                        activitiesAdapter.setData(activitiesListItems);
                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return mMainView;
    }

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = (view, position) -> {


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

}