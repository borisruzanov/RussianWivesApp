package com.borisruzanov.russianwives;

import android.view.View;

public class OnMultipleItemClickListener implements View.OnClickListener {

    private int position;
    private OnMultipleItemClickListener.OnMultipleItemClickCallback onItemClickCallback;
    private OnMultipleItemClickListener.OnChatClickCallback onItemChatCallback;
    private OnMultipleItemClickListener.OnLikeClickCallback onItemLikeCallback;

    public OnMultipleItemClickListener(int position, OnMultipleItemClickListener.OnMultipleItemClickCallback onItemClickCallback, OnMultipleItemClickListener.OnChatClickCallback onItemChatCallback, OnMultipleItemClickListener.OnLikeClickCallback onItemLikeCallback ) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
        this.onItemChatCallback = onItemChatCallback;
        this.onItemLikeCallback = onItemLikeCallback;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
        onItemChatCallback.onChatClicked(view, position);
        onItemLikeCallback.onLikeClicked(view, position);
    }


    public interface OnMultipleItemClickCallback {
        void onItemClicked(View view, int position);
    }

    public interface OnLikeClickCallback {
        void onLikeClicked(View view, int position);
    }

    public interface OnChatClickCallback {
        void onChatClicked(View view, int position);
    }


}
