package com.borisruzanov.russianwives.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.interactor.FriendProfileInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.FriendProfilePresenter;
import com.borisruzanov.russianwives.mvp.view.FriendProfileView;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendProfileActivity extends MvpAppCompatActivity implements FriendProfileView {

    //TODO change view initializations to ButterKnife binds
    Toolbar toolbar;
    FloatingActionButton fab;

    TextView nameText, ageText, countryText;
    ImageView imageView;

    Button btnAddFriend, btnStartChat;

    RecyclerView recyclerView;
    UserDescriptionListAdapter userDescriptionListAdapter;

    @InjectPresenter
    FriendProfilePresenter presenter;

    @ProvidePresenter
    public FriendProfilePresenter provideFriendProfilePresenter(){
        return new FriendProfilePresenter(new FriendProfileInteractor(new FirebaseRepository()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        //Transition
        //supportPostponeEnterTransition();
        imageView = findViewById(R.id.friend_activity_image);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getIntent().getStringExtra("transitionName");
            imageView.setTransitionName(imageTransitionName);
        }*/

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        fab = findViewById(R.id.friend_activity_fab);
        fab.setOnClickListener(v -> {
            //TODO Make sure user can make only 1 like
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                //show Registration dialog
            }
            else {
                // add like
            }
        });

        btnAddFriend = findViewById(R.id.friend_activity_btn_add_friend);
        btnAddFriend.setOnClickListener(v -> {

        });
        btnStartChat = findViewById(R.id.friend_activity_btn_start_chat);

        recyclerView = findViewById(R.id.recycler_list_friendDescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionListAdapter = new UserDescriptionListAdapter(setOnItemClickCallback());
        recyclerView.setAdapter(userDescriptionListAdapter);

        nameText = findViewById(R.id.friend_activity_tv_name);
        ageText = findViewById(R.id.friend_activity_tv_age);
        countryText = findViewById(R.id.friend_activity_tv_country);

        String friendUid = getIntent().getStringExtra("uid");
        presenter.setAllInfo(friendUid);



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFriendData(String name, String age, String country, String image) {
        nameText.setText(name);
        ageText.setText(age);
        countryText.setText(country);
        if (!image.equals(Consts.DEFAULT)) {
            Picasso.with(this)
                    .load(image)
                    .into(imageView); /*new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });*/
        }
    }

    @Override
    public void setList(List<UserDescriptionModel> userDescriptionList) {
        if (userDescriptionList.isEmpty()) {
            Log.d(Contract.TAG, "The list is empty");
        } else {
            Log.d(Contract.TAG, "The list is NOT empty");
        }
        //Setting data to the adapter
        userDescriptionListAdapter.setData(userDescriptionList);
    }

    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        return (view, position) -> {};
    }


}
