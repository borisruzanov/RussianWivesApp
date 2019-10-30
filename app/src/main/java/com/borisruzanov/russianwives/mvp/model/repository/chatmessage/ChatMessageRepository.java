package com.borisruzanov.russianwives.mvp.model.repository.chatmessage;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getDeviceToken;
import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class ChatMessageRepository {

    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();

    private StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();

    public void initChat(String friendUid) {
        if (realtimeReference.child("Chat").child(getUid()) != null){
            realtimeReference.child("Chat").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(friendUid)) {
                        Map<String, Object> chatAddMap = new HashMap<>();
                        chatAddMap.put("seen", false);
                        chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                        Map<String, Object> chatUserMap = new HashMap<>();
                        chatUserMap.put("Chat/" + getUid() + "/" + friendUid, chatAddMap);
                        chatUserMap.put("Chat/" + friendUid + "/" + getUid(), chatAddMap);

                        realtimeReference.updateChildren(chatUserMap, (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                Log.d(Contract.TAG, "initChat Error is " + databaseError.getMessage());
                            }
                        });
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

    }

    public void sendMessage(String friendUid, String message, UpdateCallback callback){
        if (!message.isEmpty()) {
            String current_user_ref = "Messages/" + getUid() + "/" + friendUid;
            String chat_user_ref = "Messages/" + friendUid + "/" + getUid();

            DatabaseReference user_message_push = realtimeReference.child("Messages")
                    .child(getUid()).child(friendUid).push();

            String push_id = user_message_push.getKey();

            //TODO Put users nameText instead of UserId
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", getUid());

            Map<String, Object> messageUserMap = new HashMap<>();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            callback.onUpdate();

            realtimeReference.child("Chat").child(getUid()).child(friendUid).child("seen").setValue(true);
            realtimeReference.child("Chat").child(getUid()).child(friendUid).child("timestamp").setValue(ServerValue.TIMESTAMP);
            realtimeReference.child("Chat").child(friendUid).child(getUid()).child("seen").setValue(false);
            realtimeReference.child("Chat").child(friendUid).child(getUid()).child("timestamp").setValue(ServerValue.TIMESTAMP);

            realtimeReference.updateChildren(messageUserMap, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    Log.d("CHAT_LOG", databaseError.getMessage());
                }
            });
        }
    }

    public void sendImage(String friendUid, Uri imageUri, UpdateCallback callback){
        final String current_user_ref = "Messages/" + getUid() + "/" + friendUid;
        final String chat_user_ref = "Messages/" + friendUid + "/" + getUid();
        // Push Message -> Token -> FrUid
        DatabaseReference user_message_push = realtimeReference.child("Messages")
                .child(getDeviceToken())
                .child(friendUid)
                .push();

        final String push_id = user_message_push.getKey();
        // get image reference
        StorageReference filePath = mImageStorage.child("message_images").child(push_id + ".jpg");


        filePath.putFile(imageUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            // Continue with the task to get the download URL
            return filePath.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String download_url = task.getResult().toString();
                Log.d("ImageDebug", "Image path is " + download_url);
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("message", download_url);
                messageMap.put("seen", false);
                messageMap.put("type", "image");
                messageMap.put("time", ServerValue.TIMESTAMP);
                messageMap.put("from", getUid());

                Map<String, Object> messageUserMap = new HashMap<>();
                messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                callback.onUpdate();

                realtimeReference.updateChildren(messageUserMap, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(Contract.TAG, "Error with insert image in DB" + databaseError.getMessage().toString());
                    }
                });
            }
        });

        //
        /*filePath.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("xxx", "inside isSuccessful");
                String download_url = filePath.getDownloadUrl().toString();
                Log.d("ImageDebug", "Image path is " + download_url);
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("message", download_url);
                messageMap.put("seen", false);
                messageMap.put("type", "image");
                messageMap.put("time", ServerValue.TIMESTAMP);
                messageMap.put("from", getUid());

                Map<String, Object> messageUserMap = new HashMap<>();
                messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                callback.onUpdate();

                realtimeReference.updateChildren(messageUserMap, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(Contract.TAG, "Error with insert image in DB" + databaseError.getMessage().toString());
                    }
                });
            }
        });*/
    }

}
