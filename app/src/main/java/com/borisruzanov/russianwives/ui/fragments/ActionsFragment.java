package com.borisruzanov.russianwives.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.Adapters.ActionsAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.ActionItem;
import com.borisruzanov.russianwives.mvp.model.interactor.ActionsInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.ActionsPresenter;
import com.borisruzanov.russianwives.mvp.view.ActionsView;
import com.borisruzanov.russianwives.ui.FriendProfileActivity;

import java.util.List;

public class ActionsFragment extends MvpAppCompatFragment implements ActionsView {

  @InjectPresenter
  ActionsPresenter actionsPresenter;

  @ProvidePresenter
  public ActionsPresenter provideActionsPresenter(){
    return new ActionsPresenter(new ActionsInteractor(new FirebaseRepository()));
  }

  RecyclerView recyclerActivitiesList;
  ActionsAdapter actionsAdapter;

  private TextView emptyText;

  public ActionsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_main_tab_activities, container, false);

    recyclerActivitiesList = view.findViewById(R.id.friends_fragment_recycler_activities);
    recyclerActivitiesList.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerActivitiesList.setHasFixedSize(true);
    recyclerActivitiesList.addItemDecoration(new DividerItemDecoration(recyclerActivitiesList.getContext(),
            DividerItemDecoration.VERTICAL));

    actionsAdapter = new ActionsAdapter(onItemClickCallback);
    recyclerActivitiesList.setAdapter(actionsAdapter);

    emptyText = view.findViewById(R.id.activities_empty_text);

    //actionsPresenter.setActionsList();

    // Inflate the layout for this fragment
    return view;
  }

  @Override
  public void showUserActions(List<ActionItem> actionItems) {
    if(!actionItems.isEmpty()) {
      emptyText.setVisibility(View.GONE);
      recyclerActivitiesList.post(() -> actionsAdapter.setData(actionItems));
    }
    else {
      recyclerActivitiesList.setVisibility(View.GONE);
      emptyText.setVisibility(View.VISIBLE);
    }
  }

  private OnItemClickListener.OnItemClickCallback onItemClickCallback = (view, position) ->
          actionsPresenter.openFriendProfile(position);

  @Override
  public void openFriendProfile(String friendUid){
    Intent friendProfileIntent = new Intent(getActivity(), FriendProfileActivity.class);
    friendProfileIntent.putExtra("uid", friendUid);
    startActivity(friendProfileIntent);
  }


}