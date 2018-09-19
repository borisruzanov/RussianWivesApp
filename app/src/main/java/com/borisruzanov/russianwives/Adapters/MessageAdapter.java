package com.borisruzanov.russianwives.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borisruzanov.russianwives.models.Message;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.GetTimeAgo;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private List<Message> mMessageList = new ArrayList<>();
    private String friendsPhoto;
    private String friendsName;

    public MessageAdapter(String friendsPhoto, String friendsName) {
        this.friendsPhoto = friendsPhoto;
        this.friendsName = friendsName;
    }

    public void setData(List<Message> mMessageList){
        this.mMessageList = mMessageList;
        notifyDataSetChanged();
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView timeView;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;
        public LinearLayout container;

        public MessageViewHolder(View view) {
            super(view);
            container = (LinearLayout) view.findViewById(R.id.message_single_layout);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            timeView = (TextView) view.findViewById(R.id.time_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        Log.d("ccc", "Photo url in adapter " + friendsPhoto);

        //TODO Rename variables
        Message currentMessage = mMessageList.get(i);
        String from_user = currentMessage.getFrom();
        String time = GetTimeAgo.getTimeAgo(currentMessage.getTime());
        String message_type = currentMessage.getType();


        viewHolder.displayName.setText(currentMessage.getFrom());
        Glide.with(viewHolder.profileImage.getContext()).load(friendsPhoto).into(viewHolder.profileImage);
        viewHolder.timeView.setText(time);
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("FsUser").child(from_user);
//        mUserDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //TODO Get reference for image + name from firestore
//                String name = dataSnapshot.child("name").setValue().toString();
//                String image = dataSnapshot.child("thumb_image").setValue().toString();
//                viewHolder.displayName.setText(name);
//                //TODO Need to be glide not picasso
//                Picasso.with(viewHolder.profileImage.getContext()).load(image)
//                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        //Edit style depends who send the message user / friend
//        Glide.with(viewHolder.profileImage.getContext()).load(friendsPhoto).into(viewHolder.messageImage);

        // Если сообщение НЕ ОТ НАС
        if (!from_user.equals(new FirebaseRepository().getUid())) {
            viewHolder.displayName.setText(friendsName);
            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);
        } else {
            // Если сообщение ОТ НАС
            viewHolder.container.setGravity(Gravity.RIGHT);
            viewHolder.displayName.setText(R.string.you);
            viewHolder.profileImage.setVisibility(View.INVISIBLE);
            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background_user);
            viewHolder.messageText.setTextColor(Color.BLACK);
        }

        //Проверка на текст или картинку
        if (message_type.equals("text")) {
            viewHolder.messageText.setText(currentMessage.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolder.profileImage.getContext()).load("https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FbgHG13B84lWEyr4voJ0dlPpROhD3%2Fprofile_photo?alt=media&token=4d0807c8-ddbc-4777-bcc5-016103d06330")
                    .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}