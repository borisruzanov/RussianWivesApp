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

    /**
     * --Hots--
     * --Male--
     * --Female--
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
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // get users from Hots/$reversedGender
    public void getHotUsers(HotUsersCallback callback) {
        Query sortQuery = hotsRef.child(prefs.getGenderSearch());
        Log.d("CarouselDebug", "Get hot users by " + prefs.getGenderSearch());
        sortQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HotUser> hotUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    HotUser hotUser = snapshot.getValue(HotUser.class);
                    hotUser.setUid(uid);
                    hotUsers.add(hotUser);
                }
                callback.setHotUsers(hotUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getHotUsersByPage(int page, HotUsersCallback callback) {
        Log.d("CarouselDebug", "In getHotUsersByPage()");
        if (page == 0) lastUserInPage = "";

        // normally loads without filters, but has problems with timestamp(duplicates) and reversedTimestamp(1 page loading)
        Query sortQuery = hotsRef.child(prefs.getGenderSearch())
                //.orderByChild("reversedTimestamp")
                .limitToLast(PAGE_SIZE);

        if (!lastUserInPage.equals("")) sortQuery = sortQuery.startAt(lastUserInPage);

        sortQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HotUser> hotUsers = new ArrayList<>();
                Log.d("CarouselDebug", "Data snapshot children count is "
                        + String.valueOf(dataSnapshot.getChildrenCount()));
                // TODO fix zero elements problem after scroll
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    Log.d("CarouselDebug", "Hot user uid is " + uid);
                    String image = snapshot.child(Consts.IMAGE).getValue().toString();
                    String timestamp = snapshot.child(Consts.TIMESTAMP).toString();
                    String reversedTimestamp = snapshot.child("reversedTimestamp").toString();
                    HotUser hotUser = new HotUser(uid, image, timestamp, reversedTimestamp);
                    Log.d("CarouselDebug", "Uid is " + uid);
                    if (!uid.equals(getUid())) hotUsers.add(hotUser);
                }

                if (!hotUsers.isEmpty()) {
                    lastUserInPage = hotUsers.get(hotUsers.size() - 1).getUid();
                    Log.d("CarouselDebug", "Last user in page uid is " + lastUserInPage);
                }
                callback.setHotUsers(hotUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
