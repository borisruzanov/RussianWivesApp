package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.ViewStateProvider;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.MvpViewState;

public class FriendProfilePresenter$$ViewStateProvider extends ViewStateProvider {
	
	@Override
	public MvpViewState<? extends MvpView> getViewState() {
		return new com.borisruzanov.russianwives.mvp.view.FriendProfileView$$State();
	}
}