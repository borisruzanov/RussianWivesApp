package com.borisruzanov.russianwives.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<User> requestsList;

    public RequestsAdapter(List<User> requestsList) {
        this.requestsList = requestsList;
    }

    @NonNull
    @Override
    public RequestsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestsItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ((RequestsItemHolder) holder).image.setText(requestsList.get(position).getImage());
        ((RequestsItemHolder) holder).name.setText(requestsList.get(position).getName());
        ((RequestsItemHolder) holder).country.setText(requestsList.get(position).getCountry());
        ((RequestsItemHolder) holder).age.setText(requestsList.get(position).getAge());
        ((RequestsItemHolder) holder).btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Contract.TAG, "Request OnClick Accept");
            }
        });
        ((RequestsItemHolder) holder).btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Contract.TAG, "Request OnClick Decline");

            }
        });

    }

    @Override
    public int getItemCount() {
            return requestsList.size();
        }

        public static class RequestsItemHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_request_name)
            TextView name;
            @BindView(R.id.item_request_country)
            TextView country;
            @BindView(R.id.item_request_age)
            TextView age;
            @BindView(R.id.item_request_image)
            ImageView image;

            @BindView(R.id.item_request_btn_accept)
            ImageView btnAccept;
            @BindView(R.id.item_request_btn_decline)
            ImageView btnDecline;

            public RequestsItemHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }
    }
