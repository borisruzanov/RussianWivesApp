package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.OnlineUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.OnlineUsersInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class OnlineUsersFragment extends MvpAppCompatFragment implements OnlineUsersView {

    OnlineUsersPresenter presenter;

    @ProvidePresenter
    public OnlineUsersPresenter getPresenter() {
        return presenter;
    }

    private OnlineUsersAdapter mAdapter;

    RecyclerView mOnlineUsersRecycler;
    SwipeRefreshLayout mRefreshSwipeLayout;
    TextView mEmptyUsersTextView;
    RelativeLayout mBottomButtonContainer;

    private boolean mIsUserExist;
    private int mCurrentPage = 1;

    private int total_item = 0;
    private int last_visible_item;
    private boolean isLoading = false;
    private boolean isMaxData = false;

    private DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();

    private String firstUid = "";
    private boolean stopDownloadList = false;

    private OnItemClickListener.OnItemClickCallback itemClickCallback = (view, position) -> {
    };
    private OnItemClickListener.OnItemClickCallback chatClickCallback = (view, position) -> {
    };
    private OnItemClickListener.OnItemClickCallback likeClickCallback = (view, position) -> {
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {

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

    @Override
    public void onStop() {
        super.onStop();
        List<OnlineUser> list = new ArrayList<>();
        int sizes = mAdapter.getItemCount();
        mAdapter.clearData(list);
        mAdapter.notifyDataSetChanged();
        int size = mAdapter.getItemCount();
        int x = size;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_users, container, false);
        ButterKnife.bind(this, view);
        setRefreshProgress(true);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        App app = (App) requireActivity().getApplication();
        app.getComponent().inject(this);
        presenter = new OnlineUsersPresenter(new OnlineUsersInteractor(new UserRepository(new Prefs(getContext()))), this);
        mOnlineUsersRecycler = (RecyclerView) getView().findViewById(R.id.online_users_rv);
        mRefreshSwipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.online_users_swipe_refresh);
        mEmptyUsersTextView = (TextView) getView().findViewById(R.id.online_users_empty_text);
        mBottomButtonContainer = (RelativeLayout) getView().findViewById(R.id.to_see_more_users_container);
        mIsUserExist = presenter.isUserExist();
        presenter.registerSubscribers();

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        Prefs prefs = new Prefs(getContext());
        mAdapter = new OnlineUsersAdapter(itemClickCallback, chatClickCallback, likeClickCallback);
        mOnlineUsersRecycler.setLayoutManager(layoutManager);
        int size = mAdapter.getItemCount();
        int x = size;
        mOnlineUsersRecycler.setAdapter(mAdapter);
        if (!prefs.getUserGender().equals("")) {
            mIsUserExist = presenter.isUserExist();
            if (mIsUserExist) {
                presenter.getOnlineFragmentUsers(mCurrentPage, mIsUserExist);
                mBottomButtonContainer.setVisibility(View.GONE);
            } else {

            }
        }
        mOnlineUsersRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                total_item = layoutManager.getItemCount();
                last_visible_item = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && last_visible_item >= (total_item - 5 )&& !stopDownloadList){
                    isLoading = true;
                    presenter.getOnlineFragmentUsers(mCurrentPage, mIsUserExist);
                    mCurrentPage++;
                }else {
                    stopDownloadList = true;
                }
            }
        });
    }

    @Override
    public void addUsers(List<OnlineUser> onlineUsers) {
        if (firstUid.equals("")) {
            firstUid = onlineUsers.get(0).getUid();
        }

        if (!firstUid.equals("") && onlineUsers.contains(firstUid)) {
            stopDownloadList = true;
        }
        if (!stopDownloadList) {
            mAdapter.addUsers(onlineUsers);
        }

        if (firstUid.equals("") && !onlineUsers.contains(firstUid)) {
            mAdapter.addUsers(onlineUsers);
        }

        if (mAdapter.getItemCount() == 0){
            mAdapter.addUsers(onlineUsers);
        }

        setRefreshProgress(false);
        isLoading = false;
        isMaxData = true;
    }

    @Override
    public void setRefreshProgress(boolean progress) {
//        mRefreshSwipeLayout.setRefreshing(progress);
    }

    @Override
    public void makeFakeUserCall() {
        presenter.getOnlineFragmentUsers(0, mIsUserExist);
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
        menOfflineList.add(new OnlineUser("pbtf1yiLwpOwLJ9Y6R7J0MFvt7C2", "David Fitch", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fpbtf1yiLwpOwLJ9Y6R7J0MFvt7C2%2Fprofile_photo?alt=media&token=df71f4bd-11c0-489b-b24f-4465da6d51ff", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("tBVa8DDfbsXA4DQ4waUDY89Db8n1", "Jeremy Stevens", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FtBVa8DDfbsXA4DQ4waUDY89Db8n1%2Fprofile_photo?alt=media&token=0d5fe67a-de8e-4522-b744-2c2ddb9ad121", "Male", "Canada", 1));
        menOfflineList.add(new OnlineUser("7E0i7WHhwXgLASgKm0oyp29spH32", "Lucas S", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F7E0i7WHhwXgLASgKm0oyp29spH32%2Fprofile_photo?alt=media&token=da0caada-2759-40fd-9fcf-a2922fcd1ff5", "Male", "Denmark", 1));
        menOfflineList.add(new OnlineUser("OdY1bZ1FBxcOct4FrXu0qlwXWvG3", "Paul Debrice", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FOdY1bZ1FBxcOct4FrXu0qlwXWvG3%2Fprofile_photo?alt=media&token=217a9589-a15d-448a-9909-a3e68bb6a7cc", "Male", "France", 1));
        menOfflineList.add(new OnlineUser("p2GiE92UzwarYlQ01JoK4Kapj3D3", "Mike Smith", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fp2GiE92UzwarYlQ01JoK4Kapj3D3%2Fprofile_photo?alt=media&token=0b45e492-16c2-4476-a682-4b3e68b29b04", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("rgbwKQnDsZSXroDcwqBX7u2qNNI2", "Mark Hetcher", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FrgbwKQnDsZSXroDcwqBX7u2qNNI2%2Fprofile_photo?alt=media&token=c207c0f4-8abd-41c1-9ef1-d5c32ebd29fb", "Male", "Canada", 1));
        menOfflineList.add(new OnlineUser("uSkQXfO4yBhpfojNHt9pTjxe0Iw2", "Sanchez Juano", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FuSkQXfO4yBhpfojNHt9pTjxe0Iw2%2Fprofile_photo?alt=media&token=e4ca6781-f18d-492b-9205-6c3b768402b6", "Male", "Mexico", 1));
        menOfflineList.add(new OnlineUser("3uuDS6mm1nOPhhBDKIVg70CMbr82", "Jerard Dutue", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F3uuDS6mm1nOPhhBDKIVg70CMbr82%2Fprofile_photo?alt=media&token=6777bfe0-3551-4546-afea-81f29cdd8bf4", "Male", "France", 1));
        menOfflineList.add(new OnlineUser("BshwjhRKHwULoPTI0pRZGu1po2q1", "John W", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FBshwjhRKHwULoPTI0pRZGu1po2q1%2Fprofile_photo?alt=media&token=516ec778-0d08-457e-93b0-e02a921bc430", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("PY8IVYwd3BYWFQlEYUuzLnzI1M32", "Leo Bonart", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FPY8IVYwd3BYWFQlEYUuzLnzI1M32%2Fprofile_photo?alt=media&token=90ed7ddb-dd25-4438-8b61-4da37e5a2fcd", "Male", "Austria", 1));
        menOfflineList.add(new OnlineUser("xDmdhY49nrYCt12OvccIMH1UBwr2", "Chris White", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FxDmdhY49nrYCt12OvccIMH1UBwr2%2Fprofile_photo?alt=media&token=ed295527-6e25-4219-88ff-a9720b234803", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("xQO5KSxiXidWZirA2ZRjPe5TMiF3", "Hern Hurtan", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FxQO5KSxiXidWZirA2ZRjPe5TMiF3%2Fprofile_photo?alt=media&token=8c8a89e6-907c-42df-be23-70978940c8b2", "Male", "Germany", 1));
        menOfflineList.add(new OnlineUser("8IEopEX1j9T9TCbKGJ3rwWXyaJT2", "Sergio Juan", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F8IEopEX1j9T9TCbKGJ3rwWXyaJT2%2Fprofile_photo?alt=media&token=f1add73d-635d-467f-b4e8-47909fd60c76", "Male", "Brazil", 1));
        menOfflineList.add(new OnlineUser("QxP0kLw4WVVUh0qjSNFxMpur56v2", "Swet Karny", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FQxP0kLw4WVVUh0qjSNFxMpur56v2%2Fprofile_photo?alt=media&token=e08f38c4-5fda-46c1-a16b-3608e7be643a", "Male", "Sweden", 1));
        menOfflineList.add(new OnlineUser("SpGE02sAIHgw4fEewNzdysQMnWB3", "Jo Herkel", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FSpGE02sAIHgw4fEewNzdysQMnWB3%2Fprofile_photo?alt=media&token=bd17d90c-d113-453a-baf5-7357132d3083", "Male", "Germany", 1));
        menOfflineList.add(new OnlineUser("djf6ZSfrIfXjdlWefmXWwnl8Fdx1", "Miguel Sancho", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fdjf6ZSfrIfXjdlWefmXWwnl8Fdx1%2Fprofile_photo?alt=media&token=9ec3143b-a24b-4a02-a51d-f43e77e8bb4f", "Male", "Spain", 1));
        menOfflineList.add(new OnlineUser("qgrecp4SnrYgAV5Zf3JLxItyv5J2", "Cisse Jerdi", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fqgrecp4SnrYgAV5Zf3JLxItyv5J2%2Fprofile_photo?alt=media&token=8a7d28fc-8f91-4dab-bbec-2543e36c5c0f", "Male", "France", 1));
        menOfflineList.add(new OnlineUser("0CYSTfDVa3QCBKoht9YBzWqOAb82", "Jorhe Jimdo", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F0CYSTfDVa3QCBKoht9YBzWqOAb82%2Fprofile_photo?alt=media&token=4552e8c9-8c80-4766-8f2f-995913ced979", "Male", "Brazil", 1));
        menOfflineList.add(new OnlineUser("4R87rgnKTCU4k5tdTHFEmo0xxKi2", "Jimmy J", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F4R87rgnKTCU4k5tdTHFEmo0xxKi2%2Fprofile_photo?alt=media&token=15d41e50-9fa0-44fd-8011-3c7e7b02b957", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("4kJtUi5JG1MszpvzIZmLQagY8zE3", "Colin Texy", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F4kJtUi5JG1MszpvzIZmLQagY8zE3%2Fprofile_photo?alt=media&token=8be0c96c-7188-4df7-bdfa-1113bdd1d40c", "Male", "Canada", 1));
        menOfflineList.add(new OnlineUser("IUxV7joi30e20sOndxAyXNrKlw82", "Giro Pindelli", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FIUxV7joi30e20sOndxAyXNrKlw82%2Fprofile_photo?alt=media&token=e04d627c-51a0-4b6f-95d7-5a50edaad16b", "Male", "Italy", 1));
        menOfflineList.add(new OnlineUser("oJKBCULkteedR3JKu1hFWaBK9uU2", "Sebastian Severn", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FoJKBCULkteedR3JKu1hFWaBK9uU2%2Fprofile_photo?alt=media&token=e965663b-f258-4fc7-81f4-1c7c5178810b", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("9f699FD0dYb576jg6O48I26ugh02", "Guano Niche", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F9f699FD0dYb576jg6O48I26ugh02%2Fprofile_photo?alt=media&token=cc59cab9-f40a-4c55-88be-e13b8b51d752", "Male", "Italy", 1));
        menOfflineList.add(new OnlineUser("aTcqqb5HatOPUfVdlcGbQyPut5f1", "Andrew Kirek", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FaTcqqb5HatOPUfVdlcGbQyPut5f1%2Fprofile_photo?alt=media&token=337844ef-3d01-47a6-af03-4cbb1cde34f3", "Male", "Canada", 1));
        menOfflineList.add(new OnlineUser("mfxN3sUvZldcplmg00D5m6pRfCh1", "Rob B", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FmfxN3sUvZldcplmg00D5m6pRfCh1%2Fprofile_photo?alt=media&token=3b5c4add-720d-44c8-a6f5-dc4f95dadfe0", "Male", "Austria", 1));
        menOfflineList.add(new OnlineUser("s55US23UOkhKGFbamxoMa1yl5IL2", "Bill Sup", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fs55US23UOkhKGFbamxoMa1yl5IL2%2Fprofile_photo?alt=media&token=4e722ec4-263e-4f9a-a603-601cc384f3da", "Male", "USA", 1));
        menOfflineList.add(new OnlineUser("uG54uC3dvkNWgiz39M1lkYLNlEY2", "Pablo B", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FuG54uC3dvkNWgiz39M1lkYLNlEY2%2Fprofile_photo?alt=media&token=0de77e65-c951-4201-a961-18f3a19cd36c", "Male", "Brazil", 1));

        for (OnlineUser user : menOfflineList) {
            poemsCatRefRus.child(user.getUid()).setValue(user);
        }
    }


}
