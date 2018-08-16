package com.borisruzanov.russianwives.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserChat;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsAdapterViewHolder> {

    private List<UserChat> userChatList = new ArrayList<>();
    private UserDescriptionListAdapter.ItemClickListener mClickListener;
    OnItemClickListener.OnItemClickCallback onItemClickCallback;
    Context context;


    public ChatsAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(List<UserChat> recipesList){
        this.userChatList = recipesList;
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
        TextView name;
        TextView seen;
        TextView time;
        ImageView image;

        public ChatsAdapterViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_list_chat_name);
            seen = (TextView) itemView.findViewById(R.id.item_list_chat_seen);
            time = (TextView) itemView.findViewById(R.id.item_list_chat_time);
            image = (ImageView) itemView.findViewById(R.id.item_list_chat_image);
        }

        @Override
        public void onClick(View v) {
        }

        void bind(UserChat model, int position){
            name.setText(model.getName());
            time.setText(String.valueOf(model.getTimestamp()));
            seen.setText(String.valueOf(model.getSeen()));
            if(model.getImage().equals("default")){
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(image);
            }else {
                Glide.with(context).load(model.getImage()).thumbnail(0.5f).into(image);
            }
            time.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
        }
    }
    public void setClickListener(UserDescriptionListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
