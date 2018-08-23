package com.borisruzanov.russianwives.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class ChatsFragment$$PresentersBinder extends PresenterBinder<com.borisruzanov.russianwives.ui.fragments.ChatsFragment> {
	public class chatsPresenterBinder extends PresenterField<com.borisruzanov.russianwives.ui.fragments.ChatsFragment> {
		public chatsPresenterBinder() {
			super("chatsPresenter", PresenterType.LOCAL, null, com.borisruzanov.russianwives.mvp.presenter.ChatsPresenter.class);
		}

		@Override
		public void bind(com.borisruzanov.russianwives.ui.fragments.ChatsFragment target, MvpPresenter presenter) {
			target.chatsPresenter = (com.borisruzanov.russianwives.mvp.presenter.ChatsPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(com.borisruzanov.russianwives.ui.fragments.ChatsFragment delegated) {
			return new com.borisruzanov.russianwives.mvp.presenter.ChatsPresenter();
		}
	}

	public List<PresenterField<com.borisruzanov.russianwives.ui.fragments.ChatsFragment>> getPresenterFields() {
		List<PresenterField<com.borisruzanov.russianwives.ui.fragments.ChatsFragment>> presenters = new ArrayList<>();

		presenters.add(new chatsPresenterBinder());

		return presenters;
	}

}
