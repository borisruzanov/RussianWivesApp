package com.borisruzanov.russianwives.ui.fragments;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.borisruzanov.russianwives.Adapters.ActionsAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.mvp.presenter.ActionsPresenter;
import com.borisruzanov.russianwives.mvp.view.ActionsView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ActionsFragment extends MvpAppCompatFragment implements ActionsView {


    @InjectPresenter
    ActionsPresenter actionsPresenter;

    RecyclerView recyclerActivitiesList;
    ActionsAdapter actionsAdapter;

    List<ActionItem> actionItems = new ArrayList<>();

    private DatabaseReference mConvDatabase;
    private String mCurrent_user_id;

    //TODO Implement Chats fragment functionality
    private View view;

    private TextView emptyText;


    public ActionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_tab_activities, container, false);

        recyclerActivitiesList = view.findViewById(R.id.friends_fragment_recycler_activities);
        recyclerActivitiesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerActivitiesList.setHasFixedSize(true);
        recyclerActivitiesList.addItemDecoration(new DividerItemDecoration(recyclerActivitiesList.getContext(), DividerItemDecoration.VERTICAL));

        actionsAdapter = new ActionsAdapter(onItemClickCallback);
        recyclerActivitiesList.setAdapter(actionsAdapter);

        emptyText = view.findViewById(R.id.activities_empty_text);

        actionsPresenter.setActionsList();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void showUserActions(List<ActionItem> actionItems) {
        if(!actionItems.isEmpty()) {
            emptyText.setVisibility(View.GONE);
            recyclerActivitiesList.post(() -> actionsAdapter.setData(actionItems));
        }
        else {
            recyclerActivitiesList.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
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

                    FragmentManager manager = getAction().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.main_frame_list, detailedFragment);
                    transaction.commit();
                }        startActivity(friendActivityIntent);*/
    };

}