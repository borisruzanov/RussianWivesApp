package com.borisruzanov.russianwives.mvp.ui.profilesettings.adapter;

        import android.content.Context;
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
        import java.util.Locale;

public class UserDescriptionEditListAdapter extends RecyclerView.Adapter<UserDescriptionEditListAdapter.UserDescriptionEditListViewHolder> {

    private List<UserDescriptionModel> userDescriptionList = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private Context mContext;

    public UserDescriptionEditListAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback, Context context) {
        this.onItemClickCallback = onItemClickCallback;
        this.mContext = context;
    }

    public void setData(List<UserDescriptionModel> recipesList) {
        this.userDescriptionList = recipesList;
    }

    @NonNull
    @Override
    public UserDescriptionEditListAdapter.UserDescriptionEditListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_description_edit_item, parent, false);
        return new UserDescriptionEditListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDescriptionEditListAdapter.UserDescriptionEditListViewHolder holder, int position) {
        UserDescriptionModel model = userDescriptionList.get(position);
        Log.d(Contract.TAG, "UserDescriptionListAdapter - onBindViewHolder");
        if (Locale.getDefault().getLanguage().equals("ru")) {
            translateToRu(holder, model.getTitle());
        } else {
            holder.title.setText(model.getTitle());
        }
        holder.item.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
    }

    private void translateToRu(UserDescriptionEditListViewHolder holder, String title) {
        if (title.equals(mContext.getString(R.string.image))) {
            holder.title.setText(mContext.getString(R.string.ru_image));
        } else if (title.equals(mContext.getString(R.string.name_text))) {
            holder.title.setText(mContext.getString(R.string.ru_name_text));
        } else if (title.equals(mContext.getString(R.string.age))) {
            holder.title.setText(mContext.getString(R.string.ru_age));
        } else if (title.equals(mContext.getString(R.string.country))) {
            holder.title.setText(mContext.getString(R.string.ru_country));
        } else if (title.equals(mContext.getString(R.string.gender))) {
            holder.title.setText(mContext.getString(R.string.ru_gender));
        } else if (title.equals(mContext.getString(R.string.relationship_status))) {
            holder.title.setText(mContext.getString(R.string.ru_relationship_status));
        } else if (title.equals(mContext.getString(R.string.ethnicity))) {
            holder.title.setText(mContext.getString(R.string.ru_ethnicity));
        } else if (title.equals(mContext.getString(R.string.faith))) {
            holder.title.setText(mContext.getString(R.string.ru_faith));
        } else if (title.equals(mContext.getString(R.string.smoke_status))) {
            holder.title.setText(mContext.getString(R.string.ru_smoke_status));
        } else if (title.equals(mContext.getString(R.string.what_drink_status))) {
            holder.title.setText(mContext.getString(R.string.ru_what_drink_status));
        } else if (title.equals(mContext.getString(R.string.do_you_want_kids))) {
            holder.title.setText(mContext.getString(R.string.ru_do_you_want_kids));
        } else if (title.equals(mContext.getString(R.string.number_of_kids))) {
            holder.title.setText(mContext.getString(R.string.ru_number_of_kids));
        } else if (title.equals(mContext.getString(R.string.looking_for))) {
            holder.title.setText(mContext.getString(R.string.ru_looking_for));
        } else if (title.equals(mContext.getString(R.string.body_type))) {
            holder.title.setText(mContext.getString(R.string.ru_body_type));
        } else {
            holder.title.setText("2222");
        }
    }

    @Override
    public int getItemCount() {
        return userDescriptionList.size();
    }

    public class UserDescriptionEditListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
