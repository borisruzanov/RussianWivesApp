package com.borisruzanov.russianwives.mvp.ui.hots;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.HotUser;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {

    private static final int VIEW_TYPE_ADD = 0;
    private List<HotUser> hotUsers = new ArrayList<>();
    private ItemClickListener mClickListener;
    private Context context;

    private int VIEW_TYPE_ITEM = 1;
    private static int VIEW_TYPE_LOADING = 2;

    // data is passed with this method
    public void setData(List<HotUser> hotUserList) {
        this.hotUsers = hotUserList;
        notifyDataSetChanged();
    }

    public void addHots(List<HotUser> hotUserList) {
        if(hotUsers.isEmpty()) hotUsers.add(new HotUser("", ""));
        hotUsers.addAll(hotUserList);
        notifyItemRangeInserted(hotUsers.size() - hotUserList.size(), hotUsers.size());
    }

    public void clearData(){
        hotUsers.clear();
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        ViewHolder holder;
        if (viewType == VIEW_TYPE_ADD) {
            view = LayoutInflater.from(context).inflate(R.layout.item_hot_first, parent, false);
            holder = new HotViewHolder(view, VIEW_TYPE_ADD);
        } else if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_second, parent, false);
            holder = new HotViewHolder(view, VIEW_TYPE_ITEM);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
            holder = new ProgressViewHolder(view);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_ADD;
        else if (hotUsers.get(position) != null) return VIEW_TYPE_ITEM;
        else return VIEW_TYPE_LOADING;
    }


    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ((HotViewHolder)holder).bind(position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return hotUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class HotViewHolder extends ViewHolder implements View.OnClickListener {
        ImageView mSecondItemImage;
        ImageView mFirstItemImage;
        LottieAnimationView lottieAnimationView;
        int viewType;

        HotViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            mSecondItemImage = itemView.findViewById(R.id.colorView);
            mFirstItemImage = itemView.findViewById(R.id.hot_first_item);
            lottieAnimationView = itemView.findViewById(R.id.lottieAnimationView);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            if (viewType == VIEW_TYPE_ADD) {
                LottieDrawable drawable = new LottieDrawable();
                LottieComposition.Factory.fromAssetFileName(context, "hot_round.json", (composition -> {
                    drawable.setComposition(composition);
                    drawable.playAnimation();
                    drawable.setScale(0.13f);
                    lottieAnimationView.setImageDrawable(drawable);
                }));
            } else {
                Glide.with(context).load(hotUsers.get(position).getImage()).thumbnail(0.5f).into(mSecondItemImage);
            }
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    class ProgressViewHolder extends ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}