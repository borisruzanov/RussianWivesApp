package com.borisruzanov.russianwives;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borisruzanov.russianwives.Models.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    //Layout
    private Toolbar mToolbar;
    private RecyclerView mUserList;

    //Firebase
    private DatabaseReference mUsersDatabase;
    public FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //Layour
        mToolbar = (Toolbar) findViewById(R.id.users_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserList = (RecyclerView) findViewById(R.id.users_list);
        //        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));

        //Firebase
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        Query query = mUsersDatabase
                .limitToLast(20);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();

        //Pass model + viewholder
        //Pass model / layout of item / ViewHolder / reference to Databace place
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <Users, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {

                String s = model.getName().toString();
                Log.v("=====> ", " " + s);
                holder.setName(model.getName());
                holder.setStatus(model.getStatus());
                holder.setImage(model.getThumb_image());

                final String userIdInList = getRef(position).getKey();
                //On Item of the list click listener
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", userIdInList);
                        startActivity(profileIntent);
                    }
                });
            }

            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_item, parent, false);
                Log.v("=====> ", "Creating View Holder ");

                return new UsersViewHolder(view);
            }
        };

        mUserList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.v("=====> IN ON START", "in onstart");
        firebaseRecyclerAdapter.startListening();

    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        View view;
        Context c;

        public UsersViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setName(String name) {

            TextView txtUserName = (TextView) view.findViewById(R.id.user_single_name);
            txtUserName.setText(name);


        }

        public void setStatus(String status) {
            TextView txtstatus = (TextView) view.findViewById(R.id.user_single_status);
            txtstatus.setText(status);
        }

        public void setImage(final String thumb_image) {

            final CircleImageView userImage = (CircleImageView) view.findViewById(R.id.user_single_img);
            ;

            Picasso.with(UsersActivity.this).load(thumb_image).placeholder(R.drawable.avatar).into(userImage);

            Picasso.with(UsersActivity.this).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).into(userImage, new Callback() {
                @Override
                public void onSuccess() {
                    // Offline Download
                }

                @Override
                public void onError() {
                    Picasso.with(c).load(thumb_image).into(userImage);
                }
            });

        }

        public View getView() {
            return view;

        }
    }
}
