package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.OnlineUser;
import com.borisruzanov.russianwives.mvp.ui.hots.InfiniteScrollListener;
import com.borisruzanov.russianwives.mvp.ui.search.adapter.FeedScrollListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineUsersFragment extends MvpAppCompatFragment implements OnlineUsersView {

    @Inject
    @InjectPresenter
    OnlineUsersPresenter presenter;

    @ProvidePresenter
    public OnlineUsersPresenter getPresenter() {
        return presenter;
    }

    private OnlineUsersAdapter adapter;

    @BindView(R.id.online_users_rv)
    RecyclerView onlineUsersRv;
    @BindView(R.id.online_users_swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.online_users_empty_text)
    TextView emptyTv;

    private FeedScrollListener scrollListener;

    private OnItemClickListener.OnItemClickCallback itemClickCallback = (view, position) -> {};
    private OnItemClickListener.OnItemClickCallback chatClickCallback = (view, position) -> {};
    private OnItemClickListener.OnItemClickCallback likeClickCallback = (view, position) -> {};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App app = (App) requireActivity().getApplication();
        app.getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("RecyclerDebug", "In OnlineUsersFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_online_users, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        setRefreshProgress(true);
        presenter.getOnlineUsers(0);
        /*refreshLayout.setOnRefreshListener(() -> {
            // TODO clear all data and load users again
            presenter.getOnlineUsers(0);
        });*/
    }

    @Override
    public void addUsers(List<OnlineUser> onlineUsers) {
        Log.d("RecyclerDebug", "In OnlineUsersFragment.addUsers()");
        adapter.addUsers(onlineUsers);
        setRefreshProgress(false);
    }

    @Override
    public void setRefreshProgress(boolean progress) {
        refreshLayout.setRefreshing(progress);
    }

    private void initRecyclerView() {
        Log.d("RecyclerDebug", "In OnlineUsersFragment.initRecyclerView()");
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        scrollListener = new FeedScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, @NotNull RecyclerView view) {
                setRefreshProgress(true);
                presenter.getOnlineUsers(page);
            }
        };

        adapter = new OnlineUsersAdapter(itemClickCallback, chatClickCallback, likeClickCallback);
        onlineUsersRv.setLayoutManager(layoutManager);
        onlineUsersRv.setAdapter(adapter);
        onlineUsersRv.addOnScrollListener(scrollListener);
    }

}
