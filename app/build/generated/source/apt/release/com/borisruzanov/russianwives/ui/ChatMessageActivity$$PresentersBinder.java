package com.borisruzanov.russianwives.ui;

import java.util.ArrayList;
import java.util.List;

import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class ChatMessageActivity$$PresentersBinder extends PresenterBinder<com.borisruzanov.russianwives.ui.ChatMessageActivity> {
	public class presenterBinder extends PresenterField<com.borisruzanov.russianwives.ui.ChatMessageActivity> {
		public presenterBinder() {
			super("presenter", PresenterType.LOCAL, null, com.borisruzanov.russianwives.mvp.presenter.ChatMessagePresenter.class);
		}

		@Override
		public void bind(com.borisruzanov.russianwives.ui.ChatMessageActivity target, MvpPresenter presenter) {
			target.presenter = (com.borisruzanov.russianwives.mvp.presenter.ChatMessagePresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(com.borisruzanov.russianwives.ui.ChatMessageActivity delegated) {
			return delegated.provideChatMessagePresenter();
		}
	}

	public List<PresenterField<com.borisruzanov.russianwives.ui.ChatMessageActivity>> getPresenterFields() {
		List<PresenterField<com.borisruzanov.russianwives.ui.ChatMessageActivity>> presenters = new ArrayList<>();

		presenters.add(new presenterBinder());

		return presenters;
	}

}
