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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.OnlineUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.ui.search.adapter.FeedScrollListener;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    private boolean mIsUserExist;

    private OnItemClickListener.OnItemClickCallback itemClickCallback = (view, position) -> {
    };
    private OnItemClickListener.OnItemClickCallback chatClickCallback = (view, position) -> {
    };
    private OnItemClickListener.OnItemClickCallback likeClickCallback = (view, position) -> {
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        App app = (App) requireActivity().getApplication();
        app.getComponent().inject(this);
//        createOfflineDBMen();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("PagerDebug", "In OnlineUsersFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_online_users, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        setRefreshProgress(true);
        mIsUserExist = presenter.isUserExist();
        presenter.registerSubscribers();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        Prefs prefs = new Prefs(getContext());
        adapter = new OnlineUsersAdapter(itemClickCallback, chatClickCallback, likeClickCallback);
        onlineUsersRv.setLayoutManager(layoutManager);
        onlineUsersRv.setAdapter(adapter);
        if (!prefs.getUserGender().equals("")) {
            mIsUserExist = true;
            presenter.getOnlineUsers(0, mIsUserExist);
        }
    }

    private void createOfflineDBWomen() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference poemsCatRefRus = database.getReference().child("FakeUsers").child("Female");
        List<OnlineUser> menOfflineList = new ArrayList<>();
        menOfflineList.add(new OnlineUser("3KTM8ZjHYEgnxXRqi9WEDeG9bXq1", "Sveta Birova", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F3KTM8ZjHYEgnxXRqi9WEDeG9bXq1%2Fprofile_photo?alt=media&token=ac3c6ba7-59a9-4884-8928-8d9dd0d3e1a5", "Female", "Belarus", 1));
        menOfflineList.add(new OnlineUser("BiTT0ITOMPctRJAYH7GzqpyIuau2", "Ira Berezina", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FBiTT0ITOMPctRJAYH7GzqpyIuau2%2Fprofile_photo?alt=media&token=9fa6bbb4-05c3-4ae3-a0e9-a467e417a3ab", "Female", "Russia", 1));
        menOfflineList.add(new OnlineUser("IbvmKd5upTNyDxr9sCT0RgIl2Rt1", "Suzanna L", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FIbvmKd5upTNyDxr9sCT0RgIl2Rt1%2Fprofile_photo?alt=media&token=5ecbe716-0e55-4f50-b445-f7b64f649c08", "Female", "Russia", 1));
        menOfflineList.add(new OnlineUser("Wuq70dyfadZUH2fiDB8OIwPfnHW2", "Gavanah S", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FWuq70dyfadZUH2fiDB8OIwPfnHW2%2Fprofile_photo?alt=media&token=2390a5bc-f4cf-49da-840b-5b61e03bd733", "Female", "Belarus", 1));
        menOfflineList.add(new OnlineUser("qW9YVFIYsqWUhfSqXE66uS4vrzw2", "Anna Pileva", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FqW9YVFIYsqWUhfSqXE66uS4vrzw2%2Fprofile_photo?alt=media&token=15d1949a-b1bd-4a9d-a2a8-3968874da70d", "Female", "Ukraine", 1));
        menOfflineList.add(new OnlineUser("u69Q5xkDYEhPlPUv8LDQhuuo51l1", "Kris N", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fu69Q5xkDYEhPlPUv8LDQhuuo51l1%2Fprofile_photo?alt=media&token=fcdca353-03d8-4a4c-8617-cc5993ba87db", "Female", "Ukraine", 1));
        menOfflineList.add(new OnlineUser("zkzqgcy7mBgkoprHlH4TNKAINqm2", "Gulmira K", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fzkzqgcy7mBgkoprHlH4TNKAINqm2%2Fprofile_photo?alt=media&token=df1a351a-0270-40f3-926e-85d4d727fde0", "Female", "Kazakhstan", 1));
        menOfflineList.add(new OnlineUser("zmhdo8JqCaOzGN80L6WdiBS6r3p2", "Yulia Grib", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fzmhdo8JqCaOzGN80L6WdiBS6r3p2%2Fprofile_photo?alt=media&token=ea41e591-5572-4180-872c-4b8520d21886", "Female", "Russia", 1));

        for (OnlineUser user : menOfflineList) {
            poemsCatRefRus.child(user.getUid()).setValue(user);
        }
    }

    private void createOfflineDBMen() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference poemsCatRefRus = database.getReference().child("FakeUsers").child("Male");
        List<OnlineUser> menOfflineList = new ArrayList<>();
        menOfflineList.add(new OnlineUser("dqLyIncyH9hxUkb3iGVWwFOmnI03", "Smith Durat", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FdqLyIncyH9hxUkb3iGVWwFOmnI03%2Fprofile_photo?alt=media&token=074b36fb-9cef-4d43-b09c-b4b21b0bbcc6", "Male", "Australia", 1));
        menOfflineList.add(new OnlineUser("JmrPxXMPEOgnAzbwNRkk3q4xmdB2", "Bob Steel", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FJmrPxXMPEOgnAzbwNRkk3q4xmdB2%2Fprofile_photo?alt=media&token=b772a8be-c2df-46cb-9075-d1529bbbae96", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("erQDqO5vDNWGzUWCtqakFv5KPt53", "John Seever", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FerQDqO5vDNWGzUWCtqakFv5KPt53%2Fprofile_photo?alt=media&token=8161b72e-9fcd-4816-bd66-1d414a49adee", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("CsZGlZsWVJbBQBbRwVbGfaOtJ4C3", "Huan Nacho", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FCsZGlZsWVJbBQBbRwVbGfaOtJ4C3%2Fprofile_photo?alt=media&token=4a325738-5dd2-42a5-b54b-e40218bb78f4", "Male", "Brazil", 1));
        menOfflineList.add(new OnlineUser("G66g3i8piWgL0RK7NHndovvRvgn2", "Chris J", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FG66g3i8piWgL0RK7NHndovvRvgn2%2Fprofile_photo?alt=media&token=f250877c-48a6-49e7-92ba-28597e381955", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("Ivc9MBYEGoRyq3hcmoYdMtdWj6l1", "Joel Sanz", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FIvc9MBYEGoRyq3hcmoYdMtdWj6l1%2Fprofile_photo?alt=media&token=f303495d-87c4-41ab-a561-88b46d8854c3", "Male", "Austria", 1));
        menOfflineList.add(new OnlineUser("Wp9O01652aMUQKFoZCN5fjVzNHj2", "Smith Thomas", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FWp9O01652aMUQKFoZCN5fjVzNHj2%2Fprofile_photo?alt=media&token=4f122932-dc51-423a-8685-3a52590cb397", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("emGVmCwGGIb7dyrbp88fbWtnT0S2", "Rob Nid", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FemGVmCwGGIb7dyrbp88fbWtnT0S2%2Fprofile_photo?alt=media&token=fc98bdd7-7a46-493c-8328-3fcf1d555851", "Male", "Canada", 1));
        menOfflineList.add(new OnlineUser("gCHU8G4EfPS4uSHR4Y45BbI57wd2", "Juan Chandez", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FgCHU8G4EfPS4uSHR4Y45BbI57wd2%2Fprofile_photo?alt=media&token=d385b6d4-95d7-4b9a-a5b0-4cdb5a40c792", "Male", "Spain", 1));
        menOfflineList.add(new OnlineUser("gITeIX5H4AaaAPk4z8IRxASL4lO2", "Andy Peres", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FgITeIX5H4AaaAPk4z8IRxASL4lO2%2Fprofile_photo?alt=media&token=048b1fac-c6ed-41e4-ba15-6efb2e47b796", "Male", "Argentina", 1));

        for (OnlineUser user : menOfflineList) {
            poemsCatRefRus.child(user.getUid()).setValue(user);
        }
    }

    @Override
    public void addUsers(List<OnlineUser> onlineUsers) {
        adapter.addUsers(onlineUsers);
        setRefreshProgress(false);
    }

    @Override
    public void setRefreshProgress(boolean progress) {
        refreshLayout.setRefreshing(progress);
    }

    @Override
    public void makeFakeUserCall() {
        presenter.getOnlineUsers(0, mIsUserExist);
    }



}
