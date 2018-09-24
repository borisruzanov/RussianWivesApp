package com.borisruzanov.russianwives.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.arellomobile.mvp.PresenterBinder;
import com.arellomobile.mvp.presenter.PresenterField;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

public class SearchFragment$$PresentersBinder extends PresenterBinder<com.borisruzanov.russianwives.ui.fragments.SearchFragment> {
	public class searchPresenterBinder extends PresenterField<com.borisruzanov.russianwives.ui.fragments.SearchFragment> {
		public searchPresenterBinder() {
			super("searchPresenter", PresenterType.LOCAL, null, com.borisruzanov.russianwives.mvp.presenter.SearchPresenter.class);
		}

		@Override
		public void bind(com.borisruzanov.russianwives.ui.fragments.SearchFragment target, MvpPresenter presenter) {
			target.searchPresenter = (com.borisruzanov.russianwives.mvp.presenter.SearchPresenter) presenter;
		}

		@Override
		public MvpPresenter<?> providePresenter(com.borisruzanov.russianwives.ui.fragments.SearchFragment delegated) {
			return delegated.providePresenter();
		}
	}

	public List<PresenterField<com.borisruzanov.russianwives.ui.fragments.SearchFragment>> getPresenterFields() {
		List<PresenterField<com.borisruzanov.russianwives.ui.fragments.SearchFragment>> presenters = new ArrayList<>();

		presenters.add(new searchPresenterBinder());

		return presenters;
	}

}
