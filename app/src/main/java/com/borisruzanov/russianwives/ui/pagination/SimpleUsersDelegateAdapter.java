package com.borisruzanov.russianwives.ui.pagination;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Users;
import com.borisruzanov.russianwives.ui.global.ViewType;
import com.borisruzanov.russianwives.ui.global.ViewTypeDelegateAdapter;
import com.bumptech.glide.Glide;

/**
 * Created by Михаил on 02.05.2018.
 */

public class SimpleUsersDelegateAdapter implements ViewTypeDelegateAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, ViewType item) {
        ((UserViewHolder)holder).bind(((Users)(item)));
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

        void bind(Users users){
            Glide.with(context).load(users.getImage()).thumbnail(0.5f).into(imageView);
            name.setText(users.getName());
            status.setText(users.getStatus());
        }
    }

}
