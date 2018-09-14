package com.borisruzanov.russianwives.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Action;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.models.ActionModel;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.view.ActionsView;
import com.borisruzanov.russianwives.utils.ActionCallback;
import com.borisruzanov.russianwives.utils.ActionWidgetCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class ActionsPresenter extends MvpPresenter<ActionsView> {

    private List<ActionItem> actionItems = new ArrayList<>();

    public void setActionsList(){
        new FirebaseRepository().getActionsInfo((visits, likes) -> {

        });

        String currentUserId = new FirebaseRepository().getUid();
        DatabaseReference actionDb = FirebaseDatabase.getInstance().getReference().child(Consts.ACTIONS_DB)
                .child(currentUserId);
        actionDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Action> actionList = new ArrayList<>();
                List<String> uidList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("ty", "action list populated");
                    //This is list of chats UID's
                    long timeStamp = Long.valueOf(snapshot.child(Consts.TIMESTAMP).getValue().toString());
                    String uid = snapshot.child(Consts.UID).getValue().toString();
                    String action = snapshot.child(Consts.ACTION).getValue().toString();
                    //This is list of Chats Objects
                    actionList.add(new Action(action, uid, timeStamp));
                    uidList.add(uid);
                }
                new FirebaseRepository().getNeededUsers(uidList, userList -> {

                    if(!userList.isEmpty() && actionItems.isEmpty()) {
                        for (int i = 0; i < userList.size(); i++) {
                            Log.d("ty", "final list populated");

                            String name = userList.get(i).getName();
                            String image = userList.get(i).getImage();
                            String action = actionList.get(i).getAction();
                            long timeStamp = actionList.get(i).getTimeStamp();

                            actionItems.add(new ActionItem(name, action, timeStamp, image));
                        }
                    }

                    /*new FirebaseRepository().getVisits(actionModels -> {
                        for (ActionModel actionModel : actionModels) {
                            Log.d("VisitsDebug", "Friend uid is " + actionModel.getUid());
                        }

                    });*/

                    /*new FirebaseRepository().getLikes(actionModels -> {
                        for (ActionModel actionModel : actionModels) {
                            Log.d("LikesDebug", "Friend uid is " + actionModel.getUid());
                        }

                    });*/

                    new FirebaseRepository().getMergedActions(actionModels -> {
                        for (ActionModel actionModel : actionModels) {
                            Log.d("ActionsDebug", "Friend uid is " + actionModel.getUid());
                        }
                    });

                    getViewState().showUserActions(actionItems);

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

//                        Log.d("vv", "Values "
//                                + actionList.get(i).getAction() + " "
//                                + actionList.get(i).getTimeStamp() + " "
//                                + userList.get(i).getImage() + " "
//                                + userList.get(i).getName());
}
