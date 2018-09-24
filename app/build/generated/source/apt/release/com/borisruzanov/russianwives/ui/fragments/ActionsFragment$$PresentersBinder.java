package com.borisruzanov.russianwives.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class ActionsFragment$$PresentersBinder extends PresenterBinder<com.borisruzanov.russianwives.ui.fragments.ActionsFragment> {
	public class actionsPresenterBinder extends PresenterField<com.borisruzanov.russianwives.ui.fragments.ActionsFragment> {
		public actionsPresenterBinder() {
			super("actionsPresenter", PresenterType.LOCAL, null, com.borisruzanov.russianwives.mvp.presenter.ActionsPresenter.class);
		}

		@Override
		public void bind(com.borisruzanov.russianwives.ui.fragments.ActionsFragment target, MvpPresenter presenter) {
			target.actionsPresenter = (com.borisruzanov.russianwives.mvp.presenter.ActionsPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(com.borisruzanov.russianwives.ui.fragments.ActionsFragment delegated) {
			return delegated.provideActionsPresenter();
		}
	}

	public List<PresenterField<com.borisruzanov.russianwives.ui.fragments.ActionsFragment>> getPresenterFields() {
		List<PresenterField<com.borisruzanov.russianwives.ui.fragments.ActionsFragment>> presenters = new ArrayList<>();

		presenters.add(new actionsPresenterBinder());

		return presenters;
	}

}
