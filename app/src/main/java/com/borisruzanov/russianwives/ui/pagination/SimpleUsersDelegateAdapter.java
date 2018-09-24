package com.borisruzanov.russianwives.ui.pagination;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.ui.global.ViewType;
import com.borisruzanov.russianwives.ui.global.ViewTypeDelegateAdapter;
import com.bumptech.glide.Glide;

public class SimpleUsersDelegateAdapter implements ViewTypeDelegateAdapter {
    //TODO REMOVE OR DELETE?
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, ViewType item) {
        ((UserViewHolder)holder).bind(((FsUser)(item)));
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, country;
        Context context;

        UserViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.user_img);
            name = itemView.findViewById(R.id.user_name);
            country = itemView.findViewById(R.id.user_country);
        }

        void bind(FsUser fsUser){
            Glide.with(context).load(fsUser.getImage()).thumbnail(0.5f).into(imageView);
            name.setText(fsUser.getName());
            country.setText(fsUser.getStatus());
        }
    }

}
