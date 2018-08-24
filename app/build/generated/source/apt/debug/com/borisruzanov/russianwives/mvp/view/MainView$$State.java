package com.borisruzanov.russianwives.mvp.view;

import java.util.Set;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.ViewCommands;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

public class MainView$$State extends MvpViewState<com.borisruzanov.russianwives.mvp.view.MainView> implements com.borisruzanov.russianwives.mvp.view.MainView {

	@Override
	public  void setViewPager() {
		SetViewPagerCommand setViewPagerCommand = new SetViewPagerCommand();
		mViewCommands.beforeApply(setViewPagerCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.MainView view : mViews) {
			view.setViewPager();
		}

		mViewCommands.afterApply(setViewPagerCommand);
	}

	@Override
	public  void callAuthWindow() {
		CallAuthWindowCommand callAuthWindowCommand = new CallAuthWindowCommand();
		mViewCommands.beforeApply(callAuthWindowCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.MainView view : mViews) {
			view.callAuthWindow();
		}

		mViewCommands.afterApply(callAuthWindowCommand);
	}

	@Override
	public  void checkingForUserInformation() {
		CheckingForUserInformationCommand checkingForUserInformationCommand = new CheckingForUserInformationCommand();
		mViewCommands.beforeApply(checkingForUserInformationCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.MainView view : mViews) {
			view.checkingForUserInformation();
		}

		mViewCommands.afterApply(checkingForUserInformationCommand);
	}


	public class SetViewPagerCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.MainView> {
		SetViewPagerCommand() {
			super("setViewPager", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.MainView mvpView) {
			mvpView.setViewPager();
		}
	}

	public class CallAuthWindowCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.MainView> {
		CallAuthWindowCommand() {
			super("callAuthWindow", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.MainView mvpView) {
			mvpView.callAuthWindow();
		}
	}

	public class CheckingForUserInformationCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.MainView> {
		CheckingForUserInformationCommand() {
			super("checkingForUserInformation", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.MainView mvpView) {
			mvpView.checkingForUserInformation();
		}
	}
}
