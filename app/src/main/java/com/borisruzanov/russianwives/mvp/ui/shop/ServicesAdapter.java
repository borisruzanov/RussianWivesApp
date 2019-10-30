package com.borisruzanov.russianwives.mvp.ui.shop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    private List<ServiceItem> mServices;
    private LayoutInflater mInflater;
    private ServicesAdapter.ItemClickListener mClickListener;
    private Context context;


    public ServicesAdapter(Context context, ArrayList<ServiceItem> colors) {
        this.mInflater = LayoutInflater.from(context);
        this.mServices = colors;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_service, parent, false);
        context = parent.getContext();
        return new ServicesAdapter.ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ServicesAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText(mServices.get(position).getmTitle());
        holder.mPrice.setText(mServices.get(position).getmPrice());
        Glide.with(context).load(mServices.get(position).getmImage()).into(holder.mImage);


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mServices.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mPrice;
        ImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTitle = itemView.findViewById(R.id.item_service_title);
            mPrice = itemView.findViewById(R.id.item_service_price);
            mImage = itemView.findViewById(R.id.item_service_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ServicesAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
