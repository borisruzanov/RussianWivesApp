package com.borisruzanov.russianwives.mvp.ui.global.adapter;

import android.content.Context;
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
import java.util.Locale;

public class UserDescriptionListAdapter extends RecyclerView.Adapter<UserDescriptionListAdapter.UserDescriptionListViewHolder> {

    private List<UserDescriptionModel> userDescriptionList = new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private Context mContext;


    public UserDescriptionListAdapter(OnItemClickListener.OnItemClickCallback onItemClickCallback, Context context) {
        this.onItemClickCallback = onItemClickCallback;
        this.mContext = context;
    }

    public void setData(List<UserDescriptionModel> recipesList) {
        this.userDescriptionList = recipesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserDescriptionListAdapter.UserDescriptionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_description_item, parent, false);
        return new UserDescriptionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDescriptionListAdapter.UserDescriptionListViewHolder holder, int position) {
        UserDescriptionModel model = userDescriptionList.get(position);
        Log.d(Contract.TAG, "UserDescriptionListAdapter - onBindViewHoldeerr");
        String s = Locale.getDefault().getLanguage();
        if (Locale.getDefault().getLanguage().equals("ru")) {
            translateTitles(holder, model.getTitle());
            translateAnswers(holder, model.getAnswerDescription());
        } else {
            holder.title.setText(model.getTitle());
            holder.description.setText(model.getAnswerDescription());
        }
        holder.description.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
    }

    private void translateAnswers(UserDescriptionListViewHolder holder, String answerDescription) {
        if (answerDescription.equals(mContext.getResources().getString(R.string.male_option))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_male_option));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.female_option))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_female_option));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.never_married))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_never_married));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.currently_separated))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_currently_separated));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.divorced))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_divorced));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.widow_widower))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_widow_widower));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.slender))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_slender));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.about_average))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_about_average));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.athletic))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_athletic));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.heavyset))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_heavyset));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.a_few_extra_pounds))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_a_few_extra_pounds));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.stocky))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_stocky));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.christian))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_christian));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.black_african_descent))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_black_african_descent));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.muslim))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_muslim));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.atheist))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_atheist));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.buddhist))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_buddist));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.adventist))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_adventist));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.other))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_other));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.black_african_descent))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_black_african_descent));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.east_indian))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_east_indian));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.latino_hispanic))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_latino_hispanic));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.native_american))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_native_american));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.white_caucasian))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_white_caucasian));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.no_way))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_no_way));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.occasionally))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_occasionally));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.daily))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_daily));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.cigar_aficionado))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_cigar_aficionado));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.yes_but_trying_to_quit))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_but_trying_to_quit));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.never_married))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_never_married));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.only_with_friends))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_only_with_friends));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.moderately))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_moderately));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.regularly))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_regularly));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.no))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_no));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.yes_they_sometimes_live_at_home))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_they_sometimes_live_at_home));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.yes_they_live_away_from_home))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_they_live_away_from_home));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.yes_they_live_at_home))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_they_live_at_home));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.definitely))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_definitely));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.someday))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_someday));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.not_sure))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_not_sure));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.probably_not))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_probably_not));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.no_but_its_ok_if_partner_has_kids))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_no_but_its_ok_if_partner_has_kids));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.default_text))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_default));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.asian))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_asian));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.never))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_never));
        } else if (answerDescription.equals(mContext.getResources().getString(R.string.default_lower_case))) {
            holder.description.setText(mContext.getResources().getString(R.string.ru_no_value));
        } else {
            holder.description.setText(answerDescription);
        }
    }

    private void translateTitles(UserDescriptionListViewHolder holder, String title) {
        if (title.equals(mContext.getResources().getString(R.string.gender))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_gender));
        } else if (title.equals(mContext.getResources().getString(R.string.relationship_status))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_relationship_status));
        } else if (title.equals(mContext.getResources().getString(R.string.body_type))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_body_type));
        } else if (title.equals(mContext.getResources().getString(R.string.ethnicity))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_ethnicity));
        } else if (title.equals(mContext.getResources().getString(R.string.faith))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_faith));
        } else if (title.equals(mContext.getResources().getString(R.string.smoke_status))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_smoke_status));
        } else if (title.equals(mContext.getResources().getString(R.string.what_drink_status))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_what_drink_status));
        } else if (title.equals(mContext.getResources().getString(R.string.number_of_kids))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_number_of_kids));
        } else if (title.equals(mContext.getResources().getString(R.string.do_you_want_kids))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_do_you_want_kids));
        } else if (title.equals(mContext.getResources().getString(R.string.looking_for))) {
            holder.title.setText(mContext.getResources().getString(R.string.ru_looking_for));
        } else {
            holder.title.setText(title);
        }

    }

    @Override
    public int getItemCount() {
        return userDescriptionList.size();
    }

    public class UserDescriptionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;

        public UserDescriptionListViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_user_profile_title);
            description = itemView.findViewById(R.id.item_user_profile_description);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
