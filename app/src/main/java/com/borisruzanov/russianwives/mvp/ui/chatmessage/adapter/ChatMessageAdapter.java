package com.borisruzanov.russianwives.mvp.ui.chatmessage.adapter;

import android.content.Context;
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
import com.borisruzanov.russianwives.GetTimeAgo;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {


    private List<Message> mMessageList = new ArrayList<>();
    private String friendsPhoto;
    private String friendsName;
    private Context context;

    public ChatMessageAdapter(String friendsPhoto, String friendsName) {
        this.friendsPhoto = friendsPhoto;
        this.friendsName = friendsName;
    }

    public void setData(List<Message> mMessageList) {
        this.mMessageList = mMessageList;
        notifyDataSetChanged();
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private TextView timeView;
        private CircleImageView profileImage;
        private TextView displayName;
        private ImageView messageImage;
        public LinearLayout container;

        public MessageViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.message_single_layout);
            messageText = view.findViewById(R.id.message_text_layout);
            timeView = view.findViewById(R.id.time_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);

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


        // If we get message not from us
        if (from_user != null) {
            if (!from_user.equals(getUid())) {
                viewHolder.displayName.setText(friendsName);
                viewHolder.messageText.setBackgroundColor(Color.WHITE);
                viewHolder.messageText.setTextColor(Color.BLACK);
                viewHolder.container.setGravity(Gravity.START);

            } else {
                // Если сообщение ОТ НАС
                viewHolder.container.setGravity(Gravity.END);
                viewHolder.displayName.setText(R.string.you);
                viewHolder.profileImage.setVisibility(View.INVISIBLE);
                viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background_user);
                viewHolder.messageText.setTextColor(Color.BLACK);
            }
        }

        //Проверка на текст или картинку
        if (message_type != null) {
            if (message_type.equals("text")) {
                viewHolder.messageText.setText(currentMessage.getMessage());
                viewHolder.messageText.setVisibility(View.VISIBLE);
                viewHolder.messageImage.setVisibility(View.GONE);
            } else {
                viewHolder.messageImage.setVisibility(View.VISIBLE);
                viewHolder.messageText.setVisibility(View.GONE);
                Picasso.with(context).load(currentMessage.getMessage()).into(viewHolder.messageImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}