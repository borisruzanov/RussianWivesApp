package com.borisruzanov.russianwives.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Users;
import com.borisruzanov.russianwives.mvp.model.interactor.SearchInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.SearchPresenter;
import com.borisruzanov.russianwives.ui.pagination.UsersAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends MvpAppCompatFragment implements com.borisruzanov.russianwives.mvp.view.SearchView {

    @InjectPresenter
    SearchPresenter searchPresenter;

    @ProvidePresenter
    public SearchPresenter providePresenter() {
        return new SearchPresenter(new SearchInteractor(new FirebaseRepository()));
    }

    RecyclerView recyclerView;
    TextView emptyText;

    View view;
    LinearLayoutManager layoutManager;
    UsersAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
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

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new UsersAdapter();
        recyclerView.setAdapter(adapter);
        searchPresenter.getUsers();

    }

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
                        //searchPresenter.getUsers(adapter.getLastItemId());
                        //Log.d("Pagination", "Statement is false inside getItemCount");
                    }
                } else Log.d("Pagination", "Statement is false");
            }
        });

        Log.d("Pagination", "In initRv()");*/

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void showError() {
        // show error widget/view
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void showUsers(final List<Users> usersList) {
        Log.d("Pagination", "In showUsers()");
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.setData(usersList);
            }
        });
       // Log.d("Pagination", String.valueOf(recyclerView.getAdapter().getItemCount()));
    }

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

}
