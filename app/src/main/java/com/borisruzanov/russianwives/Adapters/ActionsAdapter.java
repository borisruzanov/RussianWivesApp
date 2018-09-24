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

import com.borisruzanov.russianwives.GetTimeAgo;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.models.Contract;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActivitiesAdapterViewHolder> {

    private List<ActionItem> activitiesList = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private Context context;

    public ActionsAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(List<ActionItem> activitiesList) {
        this.activitiesList = activitiesList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ActionsAdapter.ActivitiesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent, false);
        return new ActionsAdapter.ActivitiesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionsAdapter.ActivitiesAdapterViewHolder holder, int position) {
        ActionItem model = activitiesList.get(position);
        holder.bind(model, position);
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    class ActivitiesAdapterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout actionContainer;
        TextView name;
        TextView time;
        TextView type_visit;
        TextView type_like;
        ImageView image;

        ActivitiesAdapterViewHolder(View itemView) {
            super(itemView);
            actionContainer = itemView.findViewById(R.id.item_action_container);
            name = itemView.findViewById(R.id.item_action_name);
            type_visit = itemView.findViewById(R.id.item_action_type_visit);
            type_like = itemView.findViewById(R.id.item_action_type_like);
            time = itemView.findViewById(R.id.item_action_time);
            image = itemView.findViewById(R.id.item_action_image);
        }

        void bind(ActionItem model, int position) {
            name.setText(model.getName());
            time.setText(GetTimeAgo.getTimeAgo(model.getTimeStamp()));
            if (model.getImage().equals("default")) {
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(image);
            } else {
                Glide.with(context).load(model.getImage()).thumbnail(0.5f).into(image);
            }
            actionContainer.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));

            if (model.getAction().equals("like")){
                type_like.setVisibility(View.VISIBLE);
                type_visit.setVisibility(View.INVISIBLE);
            } else {
                type_visit.setVisibility(View.VISIBLE);
                type_like.setVisibility(View.INVISIBLE);
            }
        }
    }

}
