package com.borisruzanov.russianwives.mvp.model.repository.rating;

import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.RatingAchCallback;
import com.borisruzanov.russianwives.utils.RatingCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class RatingRepository {

    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Consts.USERS_DB);

    public void addRating(int addPoint) {
        usersRef.child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long rating;

                if (dataSnapshot.child(Consts.RATING).exists()) {
                    rating = dataSnapshot.child(Consts.RATING).getValue(Long.class);
                    long newRating = rating + addPoint;
                    dataSnapshot.child(Consts.RATING).getRef().setValue(newRating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void addRating(int addPoint, String achievement) {
        usersRef.child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long rating = 0;
                Map<String, String> achMap = new HashMap<>();
                List<String> achList = new ArrayList<>();

                if (dataSnapshot.child(Consts.RATING).exists()) {
                    rating = dataSnapshot.child(Consts.RATING).getValue(Long.class);
                    rating += addPoint;
                }

                if (dataSnapshot.child(Consts.ACHIEVEMENTS).exists()) {
                    achList.addAll((ArrayList<String>) dataSnapshot.child(Consts.ACHIEVEMENTS).getValue());
                    achList.addAll(achMap.values());
                    for (int i = 0; i < achMap.size(); i++) {
                        Log.d("RatingDebug", achMap.get(i));
                    }
                }
                achList.add(achievement);
                dataSnapshot.child(Consts.RATING).getRef().setValue(rating);
                dataSnapshot.child(Consts.ACHIEVEMENTS).getRef().setValue(achList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getRating(String uid, RatingCallback callback) {
        usersRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Consts.RATING).exists()) {
                    long rating = dataSnapshot.child(Consts.RATING).getValue(Long.class);
                    callback.setRating(rating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getRatingAch(RatingAchCallback callback) {
        usersRef.child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long rating = 0;
                List<String> achievements = new ArrayList<>();
                if (dataSnapshot.child(Consts.RATING).exists()) {
                    rating = dataSnapshot.child(Consts.RATING).getValue(Long.class);
                }
                if (dataSnapshot.child(Consts.ACHIEVEMENTS).exists()) {
                    //Map<Object, String> achMap = (HashMap<Object, String>) dataSnapshot.getValue();
                    achievements.addAll((ArrayList<String>) dataSnapshot.child(Consts.ACHIEVEMENTS).getValue());
                }
                callback.setRatingAch(rating, achievements);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
