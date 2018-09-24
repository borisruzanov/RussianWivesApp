package com.borisruzanov.russianwives.mvp.view;

import java.util.Set;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.ViewCommands;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

public class MyProfileView$$State extends MvpViewState<com.borisruzanov.russianwives.mvp.view.MyProfileView> implements com.borisruzanov.russianwives.mvp.view.MyProfileView {

	@Override
	public  void setUserData( java.lang.String name,  java.lang.String age,  java.lang.String country,  java.lang.String image) {
		SetUserDataCommand setUserDataCommand = new SetUserDataCommand(name, age, country, image);
		mViewCommands.beforeApply(setUserDataCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.MyProfileView view : mViews) {
			view.setUserData(name, age, country, image);
		}

		mViewCommands.afterApply(setUserDataCommand);
	}

	@Override
	public  void setActionsCount( long visits,  long likes) {
		SetActionsCountCommand setActionsCountCommand = new SetActionsCountCommand(visits, likes);
		mViewCommands.beforeApply(setActionsCountCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.MyProfileView view : mViews) {
			view.setActionsCount(visits, likes);
		}

		mViewCommands.afterApply(setActionsCountCommand);
	}

	@Override
	public  void setList( java.util.List<com.borisruzanov.russianwives.models.UserDescriptionModel> userDescriptionList) {
		SetListCommand setListCommand = new SetListCommand(userDescriptionList);
		mViewCommands.beforeApply(setListCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.MyProfileView view : mViews) {
			view.setList(userDescriptionList);
		}

		mViewCommands.afterApply(setListCommand);
	}


	public class SetUserDataCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.MyProfileView> {
		public final java.lang.String name;
		public final java.lang.String age;
		public final java.lang.String country;
		public final java.lang.String image;

		SetUserDataCommand( java.lang.String name,  java.lang.String age,  java.lang.String country,  java.lang.String image) {
			super("setUserData", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.name = name;
			this.age = age;
			this.country = country;
			this.image = image;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.MyProfileView mvpView) {
			mvpView.setUserData(name, age, country, image);
		}
	}

	public class SetActionsCountCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.MyProfileView> {
		public final long visits;
		public final long likes;

		SetActionsCountCommand( long visits,  long likes) {
			super("setActionsCount", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.visits = visits;
			this.likes = likes;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.MyProfileView mvpView) {
			mvpView.setActionsCount(visits, likes);
		}
	}

	public class SetListCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.MyProfileView> {
		public final java.util.List<com.borisruzanov.russianwives.models.UserDescriptionModel> userDescriptionList;

		SetListCommand( java.util.List<com.borisruzanov.russianwives.models.UserDescriptionModel> userDescriptionList) {
			super("setList", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.userDescriptionList = userDescriptionList;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.MyProfileView mvpView) {
			mvpView.setList(userDescriptionList);
		}
	}
}
