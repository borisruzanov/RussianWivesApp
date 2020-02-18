package com.borisruzanov.russianwives.mvp.model.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.BuildConfig;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.eventbus.UpdateVersionEvent;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class SystemRepository {
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private Prefs mPrefs;

    public SystemRepository(Prefs mPrefs) {
        this.mPrefs = mPrefs;
    }

    /**
     * Checking if user need to update the app
     */
    public void checkForUpdateVersion() {
        //Making call to compare local version on device and remote version in database
        mDatabase.getReference().child("Settings").child("version").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String remoteVersion = (String) dataSnapshot.getValue();
                String versionName = BuildConfig.VERSION_NAME;
                if (!remoteVersion.equals(versionName)) {
                    //Different versions showing dialog to update
                    EventBus.getDefault().post(new UpdateVersionEvent(true));
                } else {
                    Log.d("Flow", "No need to update");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Something wrong with config");
            }
        });
    }

    /**
     * Getting config with all settings from Firebase
     */
    public void getConfig(){
        //Making a call to save in config all settings except version
        mDatabase.getReference().child("Settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String s = postSnapshot.getKey();
                    if (!postSnapshot.getKey().equals("version")){
                        mPrefs.setValue(postSnapshot.getKey(), postSnapshot.getValue().toString());
                    } else {
                        Log.d("Flow", "Version not saving");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
