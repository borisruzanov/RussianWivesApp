package com.borisruzanov.russianwives.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.borisruzanov.russianwives.zHOLD.GetTimeAgo;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsAdapterViewHolder> {

    private List<UserChat> userChatList = new ArrayList<>();
    private UserDescriptionListAdapter.ItemClickListener mClickListener;
    OnItemClickListener.OnItemClickCallback onItemClickCallback;
    Context context;

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;


    public ChatsAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback, DatabaseReference mUserDatabase, FirebaseAuth mAuth) {
        this.onItemClickCallback = onItemClickCallback;
        this.mUserDatabase = mUserDatabase;
        this.mAuth = mAuth;
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
        LinearLayout container;
        TextView name;
//        TextView seen;
        TextView time;
        TextView message;
        ImageView image;
        ImageView online;

        public ChatsAdapterViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_list_chat_name);
            container = (LinearLayout) itemView.findViewById(R.id.item_list_chat_container);
//            seen = (TextView) itemView.findViewById(R.id.item_list_chat_seen);
            time = (TextView) itemView.findViewById(R.id.item_list_chat_time);
            message = itemView.findViewById(R.id.item_list_chat_message);
            image = (ImageView) itemView.findViewById(R.id.item_list_chat_image);
            online = (ImageView) itemView.findViewById(R.id.user_single_online_icon);
        }

        @Override
        public void onClick(View v) {
        }

        void bind(UserChat model, int position){
            name.setText(model.getName());
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(model.getTimestamp(), context);
            time.setText(lastSeenTime);
            message.setText(model.getMessage());
//            seen.setText(String.valueOf(model.getSeen()));
            if(model.getImage().equals("default")){
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(image);
            }else {
                Glide.with(context).load(model.getImage()).thumbnail(0.5f).into(image);
            }
            container.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));

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

            /*if(mAuth.getCurrentUser() != null) {
                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            String onlineStatus = dataSnapshot.child("online").getValue().toString();
                            String created = dataSnapshot.child("created").getValue().toString();
                            Log.d("onlineStatus", "onlineStatus + created" + onlineStatus + " " + created);



                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }*/
        }
    }
    public void setClickListener(UserDescriptionListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
