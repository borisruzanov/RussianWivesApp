package com.borisruzanov.russianwives.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.ArrayList;
import java.util.List;

public class UserDescriptionEditListAdapter extends RecyclerView.Adapter<UserDescriptionEditListAdapter.UserDescriptionEditListViewHolder> {

    private List<UserDescriptionModel> userDescriptionList = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;

    public UserDescriptionEditListAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(List<UserDescriptionModel> recipesList){
        this.userDescriptionList = recipesList;
    }

    @NonNull
    @Override
    public UserDescriptionEditListAdapter.UserDescriptionEditListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_description_edit_item, parent, false);
        return new UserDescriptionEditListViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull UserDescriptionEditListAdapter.UserDescriptionEditListViewHolder holder, int position) {
        UserDescriptionModel model = userDescriptionList.get(position);
        Log.d(Contract.TAG, "UserDescriptionListAdapter - onBindViewHolder");
        holder.title.setText(model.getTitle());
        holder.item.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return userDescriptionList.size();
    }
    public class UserDescriptionEditListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        LinearLayout item;

        UserDescriptionEditListViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_user_edit_profile_title);
            item = itemView.findViewById(R.id.item_user_description_edit_linear);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
