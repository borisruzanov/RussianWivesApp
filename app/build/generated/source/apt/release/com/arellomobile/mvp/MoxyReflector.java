package com.arellomobile.mvp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoxyReflector {

	private static Map<Class<?>, Object> sViewStateProviders;
	private static Map<Class<?>, List<Object>> sPresenterBinders;
	private static Map<Class<?>, Object> sStrategies;

	static {
		sViewStateProviders = new HashMap<>();
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.ActionsPresenter.class, new com.borisruzanov.russianwives.mvp.presenter.ActionsPresenter$$ViewStateProvider());
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.ChatMessagePresenter.class, new com.borisruzanov.russianwives.mvp.presenter.ChatMessagePresenter$$ViewStateProvider());
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.ChatsPresenter.class, new com.borisruzanov.russianwives.mvp.presenter.ChatsPresenter$$ViewStateProvider());
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.FilterDialogPresenter.class, new com.borisruzanov.russianwives.mvp.presenter.FilterDialogPresenter$$ViewStateProvider());
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.FriendProfilePresenter.class, new com.borisruzanov.russianwives.mvp.presenter.FriendProfilePresenter$$ViewStateProvider());
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.MainPresenter.class, new com.borisruzanov.russianwives.mvp.presenter.MainPresenter$$ViewStateProvider());
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.MyProfilePresenter.class, new com.borisruzanov.russianwives.mvp.presenter.MyProfilePresenter$$ViewStateProvider());
		sViewStateProviders.put(com.borisruzanov.russianwives.mvp.presenter.SearchPresenter.class, new com.borisruzanov.russianwives.mvp.presenter.SearchPresenter$$ViewStateProvider());
		
		sPresenterBinders = new HashMap<>();
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.ChatMessageActivity.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.ChatMessageActivity$$PresentersBinder()));
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.FriendProfileActivity.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.FriendProfileActivity$$PresentersBinder()));
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.MainActivity.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.MainActivity$$PresentersBinder()));
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.MyProfileActivity.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.MyProfileActivity$$PresentersBinder()));
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.fragments.ActionsFragment.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.fragments.ActionsFragment$$PresentersBinder()));
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.fragments.ChatsFragment.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.fragments.ChatsFragment$$PresentersBinder()));
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.fragments.FilterDialogFragment.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.fragments.FilterDialogFragment$$PresentersBinder()));
		sPresenterBinders.put(com.borisruzanov.russianwives.ui.fragments.SearchFragment.class, Arrays.<Object>asList(new com.borisruzanov.russianwives.ui.fragments.SearchFragment$$PresentersBinder()));
		
		sStrategies = new HashMap<>();
		sStrategies.put(com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy.class, new com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy());
	}
	
	public static Object getViewState(Class<?> presenterClass) {
		ViewStateProvider viewStateProvider = (ViewStateProvider) sViewStateProviders.get(presenterClass);
		if (viewStateProvider == null) {
			return null;
		}
		
		return viewStateProvider.getViewState();
	}

	public static List<Object> getPresenterBinders(Class<?> delegated) {
		return sPresenterBinders.get(delegated);
	}

	public static Object getStrategy(Class<?> strategyClass) {
		return sStrategies.get(strategyClass);
	}
}
