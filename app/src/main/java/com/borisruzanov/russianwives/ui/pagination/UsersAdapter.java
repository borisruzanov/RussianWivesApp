package com.borisruzanov.russianwives.ui.pagination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Михаил on 06.05.2018.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private List<User> userList = new ArrayList<>();

    public void setData(List<User> newUsers) {
        int initialSize = userList.size();
        userList.addAll(newUsers);
        notifyItemRangeInserted(initialSize, newUsers.size());
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public String getLastItemId(){
        return userList.get(userList.size() - 1).getUid();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, status;
        Context context;

        UserViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.user_img);
            name = itemView.findViewById(R.id.user_name);
            status = itemView.findViewById(R.id.user_status);
        }

        void bind(User user){
            Glide.with(context).load(user.getImage()).thumbnail(0.5f).into(imageView);
            name.setText(user.getName());
            status.setText(user.getStatus());
        }
    }

}
