package com.borisruzanov.russianwives.mvp.model.data;

import android.support.annotation.NonNull;

import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SystemRepository {
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private Prefs mPrefs;

    public SystemRepository(Prefs mPrefs) {
        this.mPrefs = mPrefs;
    }

    /**
     * Getting config with all settings from Firebase
     */
    public void getConfig() {
        DatabaseReference mReference = mDatabase.getReference("Settings");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Saving ad_to_unlock only ones
                    if (postSnapshot.getKey().equals(Consts.APP_VERSION)) {
//                        compareVersions(postSnapshot.getValue());
                    } else {
                        mPrefs.setValue(postSnapshot.getKey(), postSnapshot.getValue().toString());
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void compareVersions(String configVersion) {
//        int configVersion =
    }

    /**
     * Checking for application version and show update message if needed
     */
    public void checkForUpdateVersion() {
        if (!mPrefs.getValue(Consts.APP_VERSION).isEmpty() || !mPrefs.getValue(Consts.APP_VERSION).equals("")) {
            int appVersion = Integer.valueOf(mPrefs.getValue(Consts.APP_VERSION));

        }
    }
}
