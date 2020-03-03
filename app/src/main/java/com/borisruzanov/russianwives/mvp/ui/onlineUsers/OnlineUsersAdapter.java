package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.OnlineUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.FirebaseUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersAdapter extends RecyclerView.Adapter<OnlineUsersAdapter.OnlineUserViewHolder> {

    private List<OnlineUser> onlineUsers = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private OnItemClickListener.OnItemClickCallback onChatClickCallback;
    private OnItemClickListener.OnItemClickCallback onLikeClickCallback;
    private Context context;

    public OnlineUsersAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback,
                              OnItemClickListener.OnItemClickCallback onChatClickCallback,
                              OnItemClickListener.OnItemClickCallback onLikeClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
        this.onChatClickCallback = onChatClickCallback;
        this.onLikeClickCallback = onLikeClickCallback;
    }

    public void addUsers(List<OnlineUser> userList) {
        int initSize = onlineUsers.size();
        onlineUsers.addAll(userList);
        notifyItemRangeChanged(initSize, userList.size());
    }

    /**
     * Clear list to avoid duplicating data in case of recreation in onPause/onStop
     * @param userList
     */
    public void clearData(List<OnlineUser> userList) {
        int oldSize = userList.size();
        onlineUsers = userList;
        notifyItemRangeRemoved(0, oldSize);
        notifyItemRangeInserted(0, userList.size());
    }

    @NonNull
    @Override
    public OnlineUsersAdapter.OnlineUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new OnlineUsersAdapter.OnlineUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineUsersAdapter.OnlineUserViewHolder holder, int position) {
        OnlineUser user = onlineUsers.get(position);
        holder.bind(user, position);
    }

    @Override
    public int getItemCount() {
        return onlineUsers.size();
    }

    class OnlineUserViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        ImageView imageView, likeBtn, chatBtn;
        TextView name, country;

        private LottieAnimationView animationView;

        OnlineUserViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            container = itemView.findViewById(R.id.item_user_container);
            imageView = itemView.findViewById(R.id.user_img);
            likeBtn = itemView.findViewById(R.id.search_btn_like);
            chatBtn = itemView.findViewById(R.id.search_btn_chat);
            name = itemView.findViewById(R.id.user_name);
            country = itemView.findViewById(R.id.user_country);
            animationView = itemView.findViewById(R.id.lottieAnimationView);
        }

        void bind(OnlineUser user, int position) {
            ViewCompat.setTransitionName(imageView, user.getName());

            if (FirebaseUtils.isUserExist() && user.getUid() != null) {
                new FriendRepository(new Prefs(context)).isLiked(user.getUid(), flag -> {
                    if (flag) {
                        likeBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
                        animationView.setVisibility(View.VISIBLE);
                    } else {
                        likeBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.heart_outline));
                        animationView.setVisibility(View.INVISIBLE);
                    }
                });
            }

            if (user.getUid() != null) {
                chatBtn.setOnClickListener(new OnItemClickListener(position, onChatClickCallback));
                likeBtn.setOnClickListener(new OnItemClickListener(position, onLikeClickCallback));
            }

            imageView.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Log.d("rating", "-> " + user.getRating());
            Log.d("uid", "-> " + onlineUsers.get(position));
            if (user.getImage().equals(Consts.DEFAULT)) {
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(imageView);
            } else {
                Glide.with(context).load(user.getImage()).thumbnail(0.5f).into(imageView);
            }

            country.setText(user.getCountry());

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
            animator.addUpdateListener(valueAnimator ->
                    animationView.setProgress((Float) valueAnimator.getAnimatedValue()));

            if (animationView.getProgress() == 0f) {
                animator.start();
            } else {
                animationView.setProgress(0f);
            }

            //adjust the height size of the item based on the width container
            container.post(new Runnable() {
                @Override
                public void run() {
                    imageView.getLayoutParams().height = container.getWidth();
                    imageView.requestLayout();
                }
            });
        }
    }

    public String getLastItemId() {
        int x = onlineUsers.size();
        String result = "";
        if (onlineUsers.size() > 0) {
            result = onlineUsers.get(onlineUsers.size() - 1).getUid();
        }
        return result;
    }

    public void removeLastItem() {
        if (onlineUsers.size() > 0) {
            onlineUsers.remove(onlineUsers.size() - 1);
        }
    }

    public long getLastItemRating() {
        return onlineUsers.get(onlineUsers.size() - 1).getRating();
    }
}
