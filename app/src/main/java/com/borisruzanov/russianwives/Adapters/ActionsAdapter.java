package com.borisruzanov.russianwives.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActivitiesAdapterViewHolder> {

    private List<ActionItem> activitiesList = new ArrayList<>();
    private UserDescriptionListAdapter.ItemClickListener mClickListener;
    OnItemClickListener.OnItemClickCallback onItemClickCallback;
    Context context;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ActionsAdapter.ActivitiesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionsAdapter.ActivitiesAdapterViewHolder holder, int position) {
        ActionItem model = activitiesList.get(position);
        Log.d(Contract.TAG, "UserDescriptionListAdapter - onBindViewHolder");
        holder.bind(model, position);
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    public class ActivitiesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView time;
        TextView type;
        ImageView image;

        public ActivitiesAdapterViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_activity_name);
            type = (TextView) itemView.findViewById(R.id.item_activity_type);
            time = (TextView) itemView.findViewById(R.id.item_activity_time);
            image = (ImageView) itemView.findViewById(R.id.item_activity_image);
        }

        @Override
        public void onClick(View v) {

        }

        void bind(ActionItem model, int position) {
            name.setText(model.getName());
            time.setText(String.valueOf(model.getTimeStamp()));
            type.setText(model.getAction());
            if (model.getImage().equals("default")) {
                Glide.with(context).load(context.getResources().getDrawable(R.drawable.default_avatar)).into(image);
            } else {
                Glide.with(context).load(model.getImage()).thumbnail(0.5f).into(image);
            }
            time.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
            Log.d("vvv", "in adapter action " + model.getAction());


//            if (model.getAction().equals("visit")){
//                Log.d("vvv", "VISIT " + model.getAction());
//            } else {
//                Log.d("vvv", "LIKE");
//            }
        }
    }

    public void setClickListener(UserDescriptionListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
