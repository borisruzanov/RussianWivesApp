package com.borisruzanov.russianwives.ui.pagination;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.Users;
import com.borisruzanov.russianwives.ui.global.ViewType;
import com.borisruzanov.russianwives.ui.global.ViewTypeDelegateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Михаил on 02.05.2018.
 */

public class UsersPagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArrayCompat<ViewTypeDelegateAdapter> delegateAdapters = new SparseArrayCompat<>();
    private List<ViewType> items = new ArrayList<>();
    private ViewType loadingItem = new ViewType() {
        @Override
        public int getViewType() {
            return Contract.LOADING;
        }
    };

    public UsersPagingAdapter(){
        delegateAdapters.put(Contract.USERS, new SimpleUsersDelegateAdapter());
        delegateAdapters.put(Contract.LOADING, new LoadingDelegateAdapter());
        items.add(loadingItem);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items.get(position));
    }

  /*  public void setData(List<Users> usersList) {
        int initPosition = items.size() - 1;
        items.remove(initPosition);
        notifyItemRemoved(initPosition);
        items.addAll(usersList);
        items.add(loadingItem);
        notifyItemRangeChanged(initPosition, items.size() + 1 *//* plus loading item *//*);
    }*/

    public void clearAndAddNews(List<Users> usersList) {
        items.clear();
        notifyItemRangeRemoved(0, getLastPosition());

        items.addAll(usersList);
        items.add(loadingItem);
        notifyItemRangeInserted(0, items.size());
    }

    public void setData(List<Users> users){
        boolean progress = isProgress();

        items.clear();
        items.addAll(users);
        if (progress) items.add(loadingItem);

        notifyDataSetChanged();
    }

    public void showProgress(boolean isVisible){
        boolean currentProgress = isProgress();
        Log.d("Pagination", "Boolean value in pagination is " + String.valueOf(currentProgress));

        if (isVisible && !currentProgress) items.add(loadingItem);
        else if (!isVisible && currentProgress) items.remove(getLastPosition());

        notifyDataSetChanged();
    }

    private boolean isProgress(){
        return !items.isEmpty() && !items.get(getLastPosition()).equals(Users.class);
    }

    private int getLastPosition(){ return items.size() -1; }

    public String getLastItemId(){
        return getUsers().get(getUsers().size() - 1).getUid();
    }

    public List<Users> getUsers() {
        List<Users> usersList = new ArrayList<>();
        for (ViewType viewType: items){
            if(viewType.getViewType() == Contract.USERS){
                usersList.add((Users)viewType);
            }
        }
        return usersList;
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
