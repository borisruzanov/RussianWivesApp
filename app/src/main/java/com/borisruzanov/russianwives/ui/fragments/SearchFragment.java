package com.borisruzanov.russianwives.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.SearchInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FilterRepository;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.SearchPresenter;
import com.borisruzanov.russianwives.ui.ChatActivity;
import com.borisruzanov.russianwives.ui.FriendProfileActivity;
import com.borisruzanov.russianwives.ui.pagination.SearchAdapter;

import java.util.List;

public class SearchFragment extends MvpAppCompatFragment implements com.borisruzanov.russianwives.mvp.view.SearchView {

    @InjectPresenter
    SearchPresenter searchPresenter;

    @ProvidePresenter
    public SearchPresenter providePresenter() {
        return new SearchPresenter(new SearchInteractor(new FirebaseRepository(),
                new FilterRepository(new Prefs(getActivity()))));
    }

    RecyclerView recyclerView;
    TextView emptyText;

    View view;
    SearchAdapter adapter;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_tab_search, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.search_recycler_view);
        emptyText = view.findViewById(R.id.search_empty_text);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);

        adapter = new SearchAdapter(onItemClickCallback, onItemChatCallback, onItemLikeCallback);
        recyclerView.setAdapter(adapter);
        searchPresenter.getFilteredList();
    }

    private OnItemClickListener.OnItemClickCallback onItemChatCallback = (View view, int position) -> {
        searchPresenter.openChat(position);
    };

    private OnItemClickListener.OnItemClickCallback onItemLikeCallback = (View view, int position) -> {
        searchPresenter.setFriendLiked(position);

    };

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = (View view, int position) -> {
        searchPresenter.openFriend(position);
    };

    /**
     * Showing error while pagination
     */
    @Override
    public void showError() {
    }

    /**
     * Open fragment with data of clicked item
     * @param uid
     * @param name
     * @param image
     */
    @Override
    public void openFriend(String uid, String name, String image) {
        Intent dataIntent = new Intent(getContext(), FriendProfileActivity.class);
        dataIntent.putExtra("uid", uid);
        startActivity(dataIntent);
        //new FirebaseRepository().addUserToActivity(new FirebaseRepository().getUid(), uid);
    }

    /**
     * Open chat with clicked friend
     * @param uid
     * @param name
     * @param image
     */
    @Override
    public void openChat(String uid, String name, String image) {
        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
        chatIntent.putExtra("uid", uid);
        chatIntent.putExtra("name", name);
        chatIntent.putExtra("photo_url", image);
        startActivity(chatIntent);
    }

    /**
     * Setting list which we received for adapter
     * @param fsUserList
     */
    @Override
    public void showUsers(final List<FsUser> fsUserList) {
        showEmpty(false);
        recyclerView.post(() -> adapter.setData(fsUserList));
    }

    /**
     * Updating results of the filtration
     */
    public void onUpdate() {
        adapter.clearData();
        searchPresenter.getFilteredList();
    }

    /**
     * If we receive empty list after filtration user see message
     * @param show
     */
    @Override
    public void showEmpty(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }

    /**
     * Method for pagination
     * @param isLoading
     */
    @Override
    public void showLoading(final boolean isLoading) {
        // for UsersPagingAdapter
      /*  adapter.showProgress(isLoading);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mTotalItemCount = layoutManager.getItemCount();
                int mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && mTotalItemCount <= (mLastVisibleItemPosition + 10)) {
                    Log.d("Pagination", "Statement is true");
                    if (adapter.getItemCount() > 0) {
                        //searchPresenter.setUsers(adapter.getLastItemId());
                        //Log.d("Pagination", "Statement is false inside getItemCount");
                    }
                } else Log.d("Pagination", "Statement is false");
            }
        });

        Log.d("Pagination", "In initRv()");*/

    }
}
