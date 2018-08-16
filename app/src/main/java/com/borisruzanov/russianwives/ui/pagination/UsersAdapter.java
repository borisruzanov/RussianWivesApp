package com.borisruzanov.russianwives.ui.pagination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
    //TODO REMOVE OR DELETE?
    private List<User> userList = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    Context context;


    public UsersAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(List<User> newUsers) {
        int initialSize = userList.size();
        userList.addAll(newUsers);
        notifyItemRangeInserted(initialSize, newUsers.size());
    }

    public void clearData(){
        userList = new ArrayList<>();
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
        User user = userList.get(position);
        holder.bind(user, position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public String getLastItemId(){
        return userList.get(userList.size() - 1).getUid();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView name, age, country;
//        Context context;

        UserViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            relativeLayout = itemView.findViewById(R.id.item_user_container);
            imageView = itemView.findViewById(R.id.user_img);
            name = itemView.findViewById(R.id.user_name);
            age = itemView.findViewById(R.id.user_age);
            country = itemView.findViewById(R.id.user_country);
        }

        void bind(User user, int position){
            relativeLayout.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
            if(user.getImage().equals("default")){
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(imageView);

            }else {
                Glide.with(context).load(user.getImage()).thumbnail(0.5f).into(imageView);
            }
            name.setText(user.getName());
            age.setText(user.getAge());
            country.setText(user.getCountry());
//            status.setText(user.getStatus());
        }
    }

}
