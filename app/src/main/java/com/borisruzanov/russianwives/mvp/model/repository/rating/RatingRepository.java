package com.borisruzanov.russianwives.mvp.model.repository.rating;

import android.support.annotation.NonNull;

import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.RatingCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class RatingRepository {

    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Consts.USERS_DB);

    public void addRating(int addPoint) {
        usersRef.child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long rating;

                if(dataSnapshot.child(Consts.RATING).exists()) {
                    rating = dataSnapshot.child(Consts.RATING).getValue(Long.class);
                    long newRating = rating + addPoint;
                    dataSnapshot.child(Consts.RATING).getRef().setValue(newRating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
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
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

}
