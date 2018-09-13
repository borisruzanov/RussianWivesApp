package com.borisruzanov.russianwives.ui.pagination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.StringsCallback;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.UserViewHolder>{
    //TODO REMOVE OR DELETE?

    String uid = new FirebaseRepository().getUid();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private List<FsUser> fsUserList = new ArrayList<>();
    /*private OnMultipleItemClickListener.OnMultipleItemClickCallback onItemClickCallback;
    private OnMultipleItemClickListener.OnChatClickCallback onChatClickCallback;
    private OnMultipleItemClickListener.OnLikeClickCallback onLikeClickCallback;*/
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private OnItemClickListener.OnItemClickCallback onChatClickCallback;
    private OnItemClickListener.OnItemClickCallback onLikeClickCallback;
    Context context;

    public SearchAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback,
                         OnItemClickListener.OnItemClickCallback onChatClickCallback,
                         OnItemClickListener.OnItemClickCallback onLikeClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
        this.onChatClickCallback = onChatClickCallback;
        this.onLikeClickCallback = onLikeClickCallback;
    }



    public void setData(List<FsUser> newFsUsers) {
        Log.d("LifecycleDebug", "List in adapter list empty is "+ newFsUsers.isEmpty());
        for (FsUser fsUser : fsUserList) {
            Log.d("LifecycleDebug", "User name in adapter is "+ fsUser.getName());
        }
        fsUserList.addAll(newFsUsers);
        notifyDataSetChanged();
    }

    public void clearData(){
        fsUserList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        FsUser fsUser = fsUserList.get(position);
        holder.bind(fsUser, position);
    }

    @Override
    public int getItemCount() {
        return fsUserList.size();
    }

    public String getLastItemId(){
        return fsUserList.get(fsUserList.size() - 1).getUid();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        ImageView imageView, like, chat;
        TextView name, country;
//        Context context;

        UserViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            container = itemView.findViewById(R.id.item_user_container);
            imageView = itemView.findViewById(R.id.user_img);
            like = itemView.findViewById(R.id.search_btn_like);
            chat = itemView.findViewById(R.id.search_btn_chat);
            name = itemView.findViewById(R.id.user_name);
//            age = itemView.findViewById(R.id.user_age);
            country = itemView.findViewById(R.id.user_country);
        }



        void bind(FsUser fsUser, int position){
//            chat.setOnClickListener(new OnMultipleItemClickListener(position, onItemClickCallback, onChatClickCallback, onLikeClickCallback));
//            like.setOnClickListener(new OnMultipleItemClickListener(position, onItemClickCallback, onChatClickCallback, onLikeClickCallback));
//            container.setOnClickListener(new OnMultipleItemClickListener(position, onItemClickCallback, onChatClickCallback, onLikeClickCallback));

            if (fsUser.getUid() != null) {
                if (mDatabase.child("Likes").child(uid).child(fsUser.getUid()) != null) {
                    mDatabase.child("Likes").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> likedList = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                likedList.add(snapshot.getKey());
                            }
                            if (likedList.contains(fsUser.getUid())) {
                                like.setImageResource(R.drawable.ic_favorite);
                                //return true value
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Log.d(Contract.SEARCH, "-------> not null");
                } else {
                    Log.d(Contract.SEARCH, "-------> null");
                }
            }

            chat.setOnClickListener(new OnItemClickListener(position, onChatClickCallback));
            like.setOnClickListener(new OnItemClickListener(position, onLikeClickCallback));
            imageView.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

//            if(fsUser.getImage().equals("default")){
//                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(imageView);
//
//            }else {
//                Glide.with(context).load(fsUser.getImage()).thumbnail(0.5f).into(imageView);
//            }
            name.setText(fsUser.getName());
//            age.setText(fsUser.getAge());
            country.setText(fsUser.getCountry());
//            status.setText(fsUser.getStatus());
        }
    }

}
