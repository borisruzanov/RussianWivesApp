package com.borisruzanov.russianwives.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.ActionItem;

import java.util.List;

public interface ActionsView extends MvpView {

    void showUserActions(List<ActionItem> actionItems);

}
