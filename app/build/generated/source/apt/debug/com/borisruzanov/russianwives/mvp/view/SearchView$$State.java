package com.borisruzanov.russianwives.mvp.view;

import java.util.Set;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.ViewCommands;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

public class SearchView$$State extends MvpViewState<com.borisruzanov.russianwives.mvp.view.SearchView> implements com.borisruzanov.russianwives.mvp.view.SearchView {

	@Override
	public  void showLoading( boolean isLoading) {
		ShowLoadingCommand showLoadingCommand = new ShowLoadingCommand(isLoading);
		mViewCommands.beforeApply(showLoadingCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.SearchView view : mViews) {
			view.showLoading(isLoading);
		}

		mViewCommands.afterApply(showLoadingCommand);
	}

	@Override
	public  void showEmpty( boolean show) {
		ShowEmptyCommand showEmptyCommand = new ShowEmptyCommand(show);
		mViewCommands.beforeApply(showEmptyCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.SearchView view : mViews) {
			view.showEmpty(show);
		}

		mViewCommands.afterApply(showEmptyCommand);
	}

	@Override
	public  void showUsers( java.util.List<com.borisruzanov.russianwives.models.User> userList) {
		ShowUsersCommand showUsersCommand = new ShowUsersCommand(userList);
		mViewCommands.beforeApply(showUsersCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.SearchView view : mViews) {
			view.showUsers(userList);
		}

		mViewCommands.afterApply(showUsersCommand);
	}

	@Override
	public  void showError() {
		ShowErrorCommand showErrorCommand = new ShowErrorCommand();
		mViewCommands.beforeApply(showErrorCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.SearchView view : mViews) {
			view.showError();
		}

		mViewCommands.afterApply(showErrorCommand);
	}


	public class ShowLoadingCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.SearchView> {
		public final boolean isLoading;

		ShowLoadingCommand( boolean isLoading) {
			super("showLoading", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.isLoading = isLoading;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.SearchView mvpView) {
			mvpView.showLoading(isLoading);
		}
	}

	public class ShowEmptyCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.SearchView> {
		public final boolean show;

		ShowEmptyCommand( boolean show) {
			super("showEmpty", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.show = show;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.SearchView mvpView) {
			mvpView.showEmpty(show);
		}
	}

	public class ShowUsersCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.SearchView> {
		public final java.util.List<com.borisruzanov.russianwives.models.User> userList;

		ShowUsersCommand( java.util.List<com.borisruzanov.russianwives.models.User> userList) {
			super("showUsers", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.userList = userList;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.SearchView mvpView) {
			mvpView.showUsers(userList);
		}
	}

	public class ShowErrorCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.SearchView> {
		ShowErrorCommand() {
			super("showError", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.SearchView mvpView) {
			mvpView.showError();
		}
	}
}
