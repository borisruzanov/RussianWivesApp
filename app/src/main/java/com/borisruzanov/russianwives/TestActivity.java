package com.borisruzanov.russianwives;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.borisruzanov.russianwives.models.OnlineUser;
import com.borisruzanov.russianwives.mvp.ui.onlineUsers.OnlineUsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    final int ITEM_LOAD_COUNT = 15;
    int total_item = 0,last_visible_item;
    OnlineUsersAdapter adapter;
    boolean isLoading= false, isMaxData= false;

    String last_node = "", last_key="";
    private boolean isGoneInOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView = findViewById(R.id.recycler_view);

        getLastKeyFromFirebase();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

//        adapter = new OnlineUsersAdapter(this);
        recyclerView.setAdapter(adapter);

        getUsers();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                total_item = layoutManager.getItemCount();
                last_visible_item = layoutManager.findLastVisibleItemPosition();

                if(!isLoading && total_item <= ((last_visible_item + ITEM_LOAD_COUNT))){
                    getUsers();
                    isLoading = true;
                }

            }
        });

        refreshListOnDataChange();



    }

    private void refreshListOnDataChange() {

        Query q = FirebaseDatabase.getInstance().getReference()
                .child("Males");

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refreshData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void refreshData() {
        isMaxData = false;
        last_node = adapter.getLastItemId();
        adapter.removeLastItem();
        adapter.notifyDataSetChanged();
        getLastKeyFromFirebase();
        getUsers();
    }

    private void getUsers() {
        if(!isMaxData){
            Query query;
            if(TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Males")
                        .orderByKey()
                        .limitToFirst(ITEM_LOAD_COUNT);

            else
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Males")
                        .orderByKey()
                        .startAt(last_node)
                        .limitToFirst(ITEM_LOAD_COUNT);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()){

                        List<OnlineUser> newUser = new ArrayList<>();
                        for(DataSnapshot userSnapShot: dataSnapshot.getChildren()){
                            newUser.add(userSnapShot.getValue(OnlineUser.class));
                        }
                        last_node = newUser.get(newUser.size() - 1).getUid();

                        if(!last_node.equals(last_key)){
                            newUser.remove(newUser.size() - 1);
                        }
                        else {
                            last_node = "end";
                            isMaxData = true;

                        }

                        adapter.addUsers(newUser);
                        isLoading = false;
                    }
                    else {
                        isLoading =false;
                        isMaxData = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void getLastKeyFromFirebase() {

        Query getLastKey = FirebaseDatabase.getInstance().getReference()
                .child("Males")
                .orderByKey()
                .limitToLast(1);

        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot lastKey : dataSnapshot.getChildren())
                {
                    last_key = lastKey.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "error, failed to get last key");

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        isGoneInOnPause = true;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isGoneInOnPause) {
            refreshData();
        }

    }
}
