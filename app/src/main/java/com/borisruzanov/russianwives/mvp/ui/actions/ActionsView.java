package com.borisruzanov.russianwives.mvp.ui.actions;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.ActionItem;

import java.util.List;

public interface ActionsView extends MvpView {

    void showUserActions(List<ActionItem> actionItems);
    void updateUserActions(List<ActionItem> newActionItems);
    void openFriendProfile(String friendUid);
}
