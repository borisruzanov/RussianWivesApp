package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.mvp.model.interactor.ActionsInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.view.ActionsView;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class ActionsPresenter extends MvpPresenter<ActionsView> {

    private ActionsInteractor interactor;
    private List<ActionItem> actionItems = new ArrayList<>();

    public ActionsPresenter(ActionsInteractor interactor) {
        this.interactor = interactor;
    }

    public void setActionsList() {
        interactor.getActions(actionList -> {
            if (!actionList.isEmpty() && actionItems.isEmpty()) {
                actionItems.addAll(actionList);
                getViewState().showUserActions(actionItems);
            }
        });

    }

    public void openFriendProfile(int position) {
        getViewState().openFriendProfile(actionItems.get(position).getUid());
    }

}