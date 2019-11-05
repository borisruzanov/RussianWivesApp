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
        String s = Locale.getDefault().getLanguage();
        if(Locale.getDefault().getLanguage().equals("ru")){
            translateTitles(holder, model.getTitle());
            translateAnswers(holder, model.getAnswerDescription());
        } else {
            holder.title.setText(model.getTitle());
            holder.description.setText(model.getAnswerDescription());
        }
        holder.description.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
    }

    private void translateAnswers(UserDescriptionListViewHolder holder, String answerDescription) {
        if(answerDescription.equals("Male")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_male_option));
        }else if(answerDescription.equals("Female")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_female_option));
        }else if(answerDescription.equals("Never married")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_never_married));
        }else if(answerDescription.equals("Currently separated")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_currently_separated));
        }else if(answerDescription.equals("Divorced")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_divorced));
        }else if(answerDescription.equals("Widow / Widower")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_widow_widower));
        }else if(answerDescription.equals("Slender")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_slender));
        }else if(answerDescription.equals("About average")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_about_average));
        }else if(answerDescription.equals("Athletic")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_athletic));
        }else if(answerDescription.equals("Heavyset")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_heavyset));
        }else if(answerDescription.equals("A few extra pounds")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_a_few_extra_pounds));
        }else if(answerDescription.equals("Stocky")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_stocky));
        }else if(answerDescription.equals("Asian")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_asian));
        }else if(answerDescription.equals("Black / African descent")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_black_african_descent));
        }else if(answerDescription.equals("East Indian")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_east_indian));
        }else if(answerDescription.equals("Latino / Hispanic")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_latino_hispanic));
        }else if(answerDescription.equals("Native American")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_native_american));
        }else if(answerDescription.equals("White / Causasian")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_white_caucasian));
        }else if(answerDescription.equals("Other")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_other));
        }else if(answerDescription.equals("No way")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_no_way));
        }else if(answerDescription.equals("Occasionally")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_occasionally));
        }else if(answerDescription.equals("Daily")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_daily));
        }else if(answerDescription.equals("Cigar aficionado")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_cigar_aficionado));
        }else if(answerDescription.equals("Yes, but trying to quit")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_but_trying_to_quit));
        }else if(answerDescription.equals("Never")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_never_married));
        }else if(answerDescription.equals("Only with friends")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_only_with_friends));
        }else if(answerDescription.equals("Moderately")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_moderately));
        }else if(answerDescription.equals("Regularly")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_regularly));
        }else if(answerDescription.equals("No")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_no));
        }else if(answerDescription.equals("Yes, they sometimes live at home")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_they_sometimes_live_at_home));
        }else if(answerDescription.equals("Yes, they live away from home")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_they_live_away_from_home));
        }else if(answerDescription.equals("Yes, they live at home")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_yes_they_live_at_home));
        }else if(answerDescription.equals("Definitely")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_definitely));
        }else if(answerDescription.equals("Someday")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_someday));
        }else if(answerDescription.equals("Not sure")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_not_sure));
        }else if(answerDescription.equals("Probably not")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_probably_not));
        }else if(answerDescription.equals("No, but its OK if partner has kids")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_no_but_its_ok_if_partner_has_kids));
        }else if(answerDescription.equals("default")){
            holder.description.setText(mContext.getResources().getString(R.string.ru_default));
        }else {
            holder.description.setText(answerDescription);
        }
    }

    private void translateTitles(UserDescriptionListViewHolder holder, String title) {
        if (title.equals("Gender")){
            holder.title.setText(mContext.getResources().getString(R.string.gender));
        } else if (title.equals("Relationship Status")){
            holder.title.setText("Семейное Положение");
        } else if (title.equals("Body Type")){
            holder.title.setText("Телосложение");
        } else if (title.equals("Ethnicity")){
            holder.title.setText("Этническая Принадлежность");
        } else if (title.equals("Faith")){
            holder.title.setText("Религия");
        } else if (title.equals("Smoke Status")){
            holder.title.setText("Курение");
        } else if (title.equals("How Often Do You Drink Alcohol")){
            holder.title.setText("Алкоголь");
        } else if (title.equals("Number Of Kids You Have")){
            holder.title.setText("Количество Детей");
        } else if (title.equals("Do You Want Kids")){
            holder.title.setText("Желание Иметь Детей");
        } else if (title.equals("Looking for")){
            holder.title.setText("В Поисках...");
        }else {
            holder.title.setText(title);
        }

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
            title = itemView.findViewById(R.id.item_user_profile_title);
            description = itemView.findViewById(R.id.item_user_profile_description);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
