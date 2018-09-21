package com.borisruzanov.russianwives.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.ArrayList;
import java.util.List;

public class UserDescriptionListAdapter extends RecyclerView.Adapter<UserDescriptionListAdapter.UserDescriptionListViewHolder> {

    private List<UserDescriptionModel> userDescriptionList = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;


    public UserDescriptionListAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(List<UserDescriptionModel> recipesList){
        this.userDescriptionList = recipesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserDescriptionListAdapter.UserDescriptionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_description_item, parent, false);
        return new UserDescriptionListViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull UserDescriptionListAdapter.UserDescriptionListViewHolder holder, int position) {
        UserDescriptionModel model = userDescriptionList.get(position);
        Log.d(Contract.TAG, "UserDescriptionListAdapter - onBindViewHoldeerr");
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getAnswerDescription());
        holder.description.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return userDescriptionList.size();
    }

    public class UserDescriptionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;

        public UserDescriptionListViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_user_profile_title);
            description = (TextView) itemView.findViewById(R.id.item_user_profile_description);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
