package com.borisruzanov.russianwives.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.borisruzanov.russianwives.Adapters.UserDescriptionEditListAdapter;
import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.UserProfileItemsListForEdit;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.ui.slider.SliderActivity;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.zHOLD.StatusActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {

    //UI
    RecyclerView recyclerView;
    Toolbar toolbar;

    //Utility
    List<UserDescriptionModel> userDescriptionEditList = new ArrayList<>();
    UserDescriptionEditListAdapter userDescriptionEditListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //UI
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);


        recyclerView = (RecyclerView) findViewById(R.id.my_profile_edit_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionEditListAdapter = new UserDescriptionEditListAdapter(setOnItemClickCallback());
        recyclerView.setAdapter(userDescriptionEditListAdapter);
        userDescriptionEditList.addAll(UserProfileItemsListForEdit.initData());
        setEditList(userDescriptionEditList);

    }

    public void setEditList(List<UserDescriptionModel> userDescriptionList) {
        //Setting data to the adapter
        userDescriptionEditListAdapter.setData(userDescriptionList);
        //Refreshing UI of the recycler with new data
        userDescriptionEditListAdapter.notifyDataSetChanged();
    }

    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        return (view, position) -> {
            UserDescriptionModel itemClicked = userDescriptionEditList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("field_id", itemClicked.getTitle());
            Log.d(Contract.SLIDER, "Id from list clicked is - " + itemClicked.getTitle());
            bundle.putString("intent", "list");
            Intent sliderIntent = new Intent(this, SliderActivity.class);
            sliderIntent.putExtras(bundle);
            startActivity(sliderIntent);
        };
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
}
