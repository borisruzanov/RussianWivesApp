package com.borisruzanov.russianwives.mvp.ui.chats.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserChat;
import com.borisruzanov.russianwives.GetTimeAgo;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsAdapterViewHolder> {

    private List<UserChat> userChatList = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private Context context;

    public ChatsAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(List<UserChat> userChatList){
        this.userChatList = userChatList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatsAdapter.ChatsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chat, parent, false);
        return new ChatsAdapter.ChatsAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapterViewHolder holder, int position) {
        UserChat model = userChatList.get(position);
        Log.d(Contract.TAG, "UserDescriptionListAdapter - onBindViewHolder");
        holder.bind(model, position);
    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }

    public class ChatsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout container;
        TextView name;
        TextView time;
        TextView message;
        ImageView image;
        ImageView online;

        public ChatsAdapterViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_list_chat_name);
            container = itemView.findViewById(R.id.item_list_chat_container);
            time = itemView.findViewById(R.id.item_list_chat_time);
            message = itemView.findViewById(R.id.item_list_chat_message);
            image = itemView.findViewById(R.id.item_list_chat_image);
            online = itemView.findViewById(R.id.user_single_online_icon);
        }

        @Override
        public void onClick(View v) {
        }

        void bind(UserChat model, int position){
            name.setText(model.getName());
            Long l = 1535047947581L;
            Log.d("TimestampDebug", "Timestamp is " + model.getMessageTimestamp());
            String lastSeenTime = GetTimeAgo.getTimeAgo(Long.valueOf(model.getMessageTimestamp()));
            time.setText(lastSeenTime);

            if (model.getMessage().contains(Consts.IMAGE_STORAGE)) {
               message.setText(context.getString(R.string.photo));
               message.setTextColor(ContextCompat.getColor(context, R.color.msg_photo_color));
            } else {
                message.setText(model.getMessage());
            }

            if(model.getImage().equals("default")){
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(image);
            }else {
                Glide.with(context).load(model.getImage()).thumbnail(0.5f).into(image);
            }
            container.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));

            if (!model.getSeen()) container.setBackgroundColor(ContextCompat.getColor(context, R.color.chat_item_bg));
            else container.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

            if(model.getOnline() != null){
            if (model.getOnline().equals("true")) online.setVisibility(View.VISIBLE);
            else {
                Log.d("xx", "onlineStatus.equals(\"FAlse\")");
                online.setVisibility(View.INVISIBLE);
            }
            } else {
                Log.d("xx", "onlineStatus.equals(\"FAlse\")");
                online.setVisibility(View.INVISIBLE);

            }
        }
    }

}
