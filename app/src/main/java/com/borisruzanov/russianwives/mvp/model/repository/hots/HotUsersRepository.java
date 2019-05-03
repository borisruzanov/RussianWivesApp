package com.borisruzanov.russianwives.mvp.model.repository.hots;

import android.support.annotation.NonNull;
import android.util.Log;

import com.borisruzanov.russianwives.models.HotUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.HotUsersCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class HotUsersRepository {

    private Prefs prefs;
    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersRef = realtimeReference.child(Consts.USERS_DB);
    private DatabaseReference hotsRef = realtimeReference.child("Hots");

    private String lastUserInPage;
    private int PAGE_SIZE = 5;

    public HotUsersRepository(Prefs prefs) {
        this.prefs = prefs;
    }

    /** --Hots--
     --Male--
     --Female--
     */

    // add user to gender equal to preferences value
    public void setUserInHot() {
        if (getUid() != null) {
            usersRef.child(getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.child(Consts.IMAGE).getValue().toString();
                    Map<String, Object> hotMap = new HashMap<>();
                    hotMap.put(Consts.IMAGE, image);
                    hotMap.put(Consts.TIMESTAMP, ServerValue.TIMESTAMP);
                    hotMap.put("reversedTimestamp", -1 * new Date().getTime());
                    deleteEarliestUser(prefs.getGender());
                    hotsRef.child(prefs.getGender()).child(getUid()).updateChildren(hotMap);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    // delete earliest hot user if list length == N + 1
    private void deleteEarliestUser(String gender) {
        Query timeQuery = hotsRef.child(gender).orderByChild(Consts.TIMESTAMP);
        timeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                Log.d("CarouselDebug", "Hots " + gender + " list has " + count + " elements");
                if (count >= 101) {
                    DataSnapshot earliestUser = dataSnapshot.getChildren().iterator().next();
                    String delUid = earliestUser.child(Consts.UID).getKey();
                    Log.d("CarouselDebug", "Deleted uid is " + delUid);
                    earliestUser.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    // get users from Hots/$reversedGender
    public void getHotUsers(HotUsersCallback callback) {
        Query sortQuery = hotsRef.child(prefs.getGenderSearch()).orderByChild("reversedTimestamp");
        sortQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HotUser> hotUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String image = snapshot.child(Consts.IMAGE).getValue().toString();
                    hotUsers.add(new HotUser(uid, image));
                }
                callback.setHotUsers(hotUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void getHotsByPage(int page, HotUsersCallback callback) {
        if (page == 0) lastUserInPage = "";

        Query sortQuery = hotsRef.child(prefs.getGenderSearch())
                .orderByChild("reversedTimestamp")
                .startAt(lastUserInPage)
                .limitToFirst(PAGE_SIZE);

        sortQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HotUser> hotUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String image = snapshot.child(Consts.IMAGE).getValue().toString();
                    if (!uid.equals(getUid())) hotUsers.add(new HotUser(uid, image));
                }
                lastUserInPage = hotUsers.get(hotUsers.size() - 1).getUid();
                callback.setHotUsers(hotUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }}
