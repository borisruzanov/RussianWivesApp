package com.borisruzanov.russianwives.ui;

import java.util.ArrayList;
import java.util.List;

import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class FriendProfileActivity$$PresentersBinder extends PresenterBinder<com.borisruzanov.russianwives.ui.FriendProfileActivity> {
	public class presenterBinder extends PresenterField<com.borisruzanov.russianwives.ui.FriendProfileActivity> {
		public presenterBinder() {
			super("presenter", PresenterType.LOCAL, null, com.borisruzanov.russianwives.mvp.presenter.FriendProfilePresenter.class);
		}

		@Override
		public void bind(com.borisruzanov.russianwives.ui.FriendProfileActivity target, MvpPresenter presenter) {
			target.presenter = (com.borisruzanov.russianwives.mvp.presenter.FriendProfilePresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(com.borisruzanov.russianwives.ui.FriendProfileActivity delegated) {
			return delegated.provideFriendProfilePresenter();
		}
	}

	public List<PresenterField<com.borisruzanov.russianwives.ui.FriendProfileActivity>> getPresenterFields() {
		List<PresenterField<com.borisruzanov.russianwives.ui.FriendProfileActivity>> presenters = new ArrayList<>();

		presenters.add(new presenterBinder());

		return presenters;
	}

}
