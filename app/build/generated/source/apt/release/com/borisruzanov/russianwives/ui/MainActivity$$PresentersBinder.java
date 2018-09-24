package com.borisruzanov.russianwives.ui;

import java.util.ArrayList;
import java.util.List;

import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class MainActivity$$PresentersBinder extends PresenterBinder<com.borisruzanov.russianwives.ui.MainActivity> {
	public class mainPresenterBinder extends PresenterField<com.borisruzanov.russianwives.ui.MainActivity> {
		public mainPresenterBinder() {
			super("mainPresenter", PresenterType.LOCAL, null, com.borisruzanov.russianwives.mvp.presenter.MainPresenter.class);
		}

		@Override
		public void bind(com.borisruzanov.russianwives.ui.MainActivity target, MvpPresenter presenter) {
			target.mainPresenter = (com.borisruzanov.russianwives.mvp.presenter.MainPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(com.borisruzanov.russianwives.ui.MainActivity delegated) {
			return delegated.provideMainPresenter();
		}
	}

	public List<PresenterField<com.borisruzanov.russianwives.ui.MainActivity>> getPresenterFields() {
		List<PresenterField<com.borisruzanov.russianwives.ui.MainActivity>> presenters = new ArrayList<>();

		presenters.add(new mainPresenterBinder());

		return presenters;
	}

}
