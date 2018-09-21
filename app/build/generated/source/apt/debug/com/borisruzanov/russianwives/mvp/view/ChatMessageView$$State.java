package com.borisruzanov.russianwives.mvp.view;

import java.util.Set;

import com.arellomobile.mvp.viewstate.MvpViewState;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.ViewCommands;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

public class ChatMessageView$$State extends MvpViewState<com.borisruzanov.russianwives.mvp.view.ChatMessageView> implements com.borisruzanov.russianwives.mvp.view.ChatMessageView {

	@Override
	public  void setEmptyMessage() {
		SetEmptyMessageCommand setEmptyMessageCommand = new SetEmptyMessageCommand();
		mViewCommands.beforeApply(setEmptyMessageCommand);

		if (mViews == null || mViews.isEmpty()) {
			return;
		}

		for(com.borisruzanov.russianwives.mvp.view.ChatMessageView view : mViews) {
			view.setEmptyMessage();
		}

		mViewCommands.afterApply(setEmptyMessageCommand);
	}


	public class SetEmptyMessageCommand extends ViewCommand<com.borisruzanov.russianwives.mvp.view.ChatMessageView> {
		SetEmptyMessageCommand() {
			super("setEmptyMessage", com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class);
		}

		@Override
		public void apply(com.borisruzanov.russianwives.mvp.view.ChatMessageView mvpView) {
			mvpView.setEmptyMessage();
		}
	}
}
