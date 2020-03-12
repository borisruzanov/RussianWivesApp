package com.borisruzanov.russianwives.mvp.ui.profilesettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsListForEdit;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfileActivity;
import com.borisruzanov.russianwives.mvp.ui.profilesettings.adapter.UserDescriptionEditListAdapter;
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity {

    //UI
    RecyclerView recyclerView;
    Toolbar toolbar;
    SwitchCompat mIncognitoSwitcher;

    //Utility
    List<UserDescriptionModel> userDescriptionEditList = new ArrayList<>();
    UserDescriptionEditListAdapter userDescriptionEditListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //UI
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);


        recyclerView = findViewById(R.id.my_profile_edit_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionEditListAdapter = new UserDescriptionEditListAdapter(setOnItemClickCallback(), this);
        recyclerView.setAdapter(userDescriptionEditListAdapter);
        userDescriptionEditList.addAll(UserProfileItemsListForEdit.initData());
        setEditList(userDescriptionEditList);
        mIncognitoSwitcher = findViewById(R.id.my_profile_edit_incognito_switcher);
        mIncognitoSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    Log.d("test", "incognito on");
                } else {
                    Log.d("test", "incognito off");
                }
            }
        });
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

    @Override
    public void onBackPressed() {
        finish();
        Intent profileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(profileIntent);
    }
}
