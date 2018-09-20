package com.borisruzanov.russianwives.mvp.view;

import java.util.Set;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.ViewCommands;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

public class FriendProfileView$$State extends MvpViewState<com.borisruzanov.russianwives.mvp.view.FriendProfileView> implements com.borisruzanov.russianwives.mvp.view.FriendProfileView {

	@Override
	public  void setFriendData( java.lang.String name,  java.lang.String age,  java.lang.String country,  java.lang.String image) {
		SetFriendDataCommand setFriendDataCommand = new SetFriendDataCommand(name, age, country, image);
		mViewCommands.beforeApply(setFriendDataCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.FriendProfileView view : mViews) {
			view.setFriendData(name, age, country, image);
		}

		mViewCommands.afterApply(setFriendDataCommand);
	}

	@Override
	public  void setList( java.util.List<com.borisruzanov.russianwives.models.UserDescriptionModel> userDescriptionList) {
		SetListCommand setListCommand = new SetListCommand(userDescriptionList);
		mViewCommands.beforeApply(setListCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.FriendProfileView view : mViews) {
			view.setList(userDescriptionList);
		}

		mViewCommands.afterApply(setListCommand);
	}


	public class SetFriendDataCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.FriendProfileView> {
		public final java.lang.String name;
		public final java.lang.String age;
		public final java.lang.String country;
		public final java.lang.String image;

		SetFriendDataCommand( java.lang.String name,  java.lang.String age,  java.lang.String country,  java.lang.String image) {
			super("setFriendData", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.name = name;
			this.age = age;
			this.country = country;
			this.image = image;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.FriendProfileView mvpView) {
			mvpView.setFriendData(name, age, country, image);
		}
	}

	public class SetListCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.FriendProfileView> {
		public final java.util.List<com.borisruzanov.russianwives.models.UserDescriptionModel> userDescriptionList;

		SetListCommand( java.util.List<com.borisruzanov.russianwives.models.UserDescriptionModel> userDescriptionList) {
			super("setList", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.userDescriptionList = userDescriptionList;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.FriendProfileView mvpView) {
			mvpView.setList(userDescriptionList);
		}
	}
}
