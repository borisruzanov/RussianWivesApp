package com.borisruzanov.russianwives.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.SearchInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FilterRepository;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.SearchPresenter;
import com.borisruzanov.russianwives.ui.pagination.UsersAdapter;
import com.borisruzanov.russianwives.ui.ChatActivity;

import java.util.List;

public class SearchFragment extends MvpAppCompatFragment implements com.borisruzanov.russianwives.mvp.view.SearchView {

    //TODO Implement Chats fragment functionality

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

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);

        adapter = new UsersAdapter(onItemClickCallback);
        recyclerView.setAdapter(adapter);
        searchPresenter.getFilteredList();

    }

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = (view, position) -> {
        Log.d(Contract.TAG, "In onCLICK");
        searchPresenter.openFriend(position);
       /* Intent friendActivityIntent = new Intent(getContext(), FriendActivity.class);
                User itemClicked = userList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("image", itemClicked.getImage());
                bundle.putString("name", itemClicked.getName());
                bundle.putParcelableArrayList("ingredients", new ArrayList<Parcelable>(itemClicked.getIngredients()));
                bundle.putParcelableArrayList("steps", new ArrayList<Parcelable>(itemClicked.getSteps()));
                bundle.putInt("swrvings", itemClicked.getServings());

                if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                    Log.d(TAG_WORK_CHECKING, "It is more than 600 dp");
                    Intent dataIntent = new Intent(getContext(), TabletActivity.class);
                    dataIntent.putExtras(bundle);
                    startActivity(dataIntent);

                } else {

                    DetailedFragment detailedFragment = new DetailedFragment();
                    detailedFragment.setArguments(bundle);

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.main_frame_list, detailedFragment);
                    transaction.commit();
                }        startActivity(friendActivityIntent);*/
    };

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
    public void showError() {
        // show error widget/view
    }

    @Override
    public void openFriend(String uid, String name, String image) {
        Log.d(Contract.TAG, "-----> input UID " + uid + " " + image + " " + name);
//        Intent dataIntent = new Intent(getContext(), FriendActivity.class);
//        dataIntent.putExtra("uid", uid);
//        startActivity(dataIntent);

        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
        chatIntent.putExtra("uid", uid);
        chatIntent.putExtra("name",name);
        chatIntent.putExtra("photo_url", image);
        startActivity(chatIntent);
    }

    @Override
    public void showUsers(final List<User> userList) {
        Log.d("Pagination", "In showUsers()");
        recyclerView.post(() -> adapter.setData(userList));
    }

    public void onUpdate(){
        adapter.clearData();
        searchPresenter.getFilteredList();
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
