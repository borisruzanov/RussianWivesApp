package com.borisruzanov.russianwives.mvp.view;

import java.util.Set;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.ViewCommands;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

public class FilterView$$State extends MvpViewState<com.borisruzanov.russianwives.mvp.view.FilterView> implements com.borisruzanov.russianwives.mvp.view.FilterView {

	@Override
	public  void getSavedValues( java.util.List<java.lang.String> stringList,  java.util.List<java.lang.Integer> resIdList) {
		GetSavedValuesCommand getSavedValuesCommand = new GetSavedValuesCommand(stringList, resIdList);
		mViewCommands.beforeApply(getSavedValuesCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.FilterView view : mViews) {
			view.getSavedValues(stringList, resIdList);
		}

		mViewCommands.afterApply(getSavedValuesCommand);
	}


	public class GetSavedValuesCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.FilterView> {
		public final java.util.List<java.lang.String> stringList;
		public final java.util.List<java.lang.Integer> resIdList;

		GetSavedValuesCommand( java.util.List<java.lang.String> stringList,  java.util.List<java.lang.Integer> resIdList) {
			super("getSavedValues", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
			this.stringList = stringList;
			this.resIdList = resIdList;
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.FilterView mvpView) {
			mvpView.getSavedValues(stringList, resIdList);
		}
	}
}
