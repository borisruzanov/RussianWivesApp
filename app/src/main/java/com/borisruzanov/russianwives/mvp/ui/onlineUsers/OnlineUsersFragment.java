package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.OnlineUser;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.OnlineUsersInteractor;
import com.borisruzanov.russianwives.mvp.model.interactor.coins.CoinsInteractor;
import com.borisruzanov.russianwives.mvp.model.interactor.search.SearchInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.coins.CoinsRepository;
import com.borisruzanov.russianwives.mvp.model.repository.filter.FilterRepository;
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository;
import com.borisruzanov.russianwives.mvp.model.repository.hots.HotUsersRepository;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity;
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity;
import com.borisruzanov.russianwives.mvp.ui.main.MainScreenActivity;
import com.borisruzanov.russianwives.mvp.ui.search.SearchPresenter;
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.ButterKnife;

import static com.borisruzanov.russianwives.utils.Consts.ITEM_LOAD_COUNT;


public class OnlineUsersFragment extends Fragment implements OnlineUsersView, ConfirmDialogFragment.ConfirmListener {

    private OnlineUsersPresenter mPresenter;
    private SearchPresenter mSearchPresenter;
    private LinearLayoutManager layoutManager;

    private List<OnlineUser> mUserList = new ArrayList<>();
    Set<OnlineUser> mFilteredList = new LinkedHashSet<>();
    private OnlineUsersAdapter mAdapter;

    private RecyclerView mOnlineUsersRecycler;
    private TextView mEmptyUsersTextView;
    private RelativeLayout mBottomButtonContainer;
    private Button mSeeMoreRegisterButton;
    private List<OnlineUser> filteredList;
    private ProgressBar mProgressBar;
    private FirebaseAnalytics firebaseAnalytics;

    private boolean mIsUserExist;
    private int mCurrentPage = 1;

    //Pagination fields

    private int total_item = 0;
    private int last_visible_item = 0;
    private boolean isLoading = false;
    private boolean isMaxData = false;
    String last_node = "";
    String last_key = "";
    private String firstUid = "";
    private boolean stopDownloadList = false;
    private boolean isGoneInOnPause;
    private long last_rating = 0;
    Prefs mPrefs;

    //Recommended user
    private ImageView mRecommendedImage;
    private CardView mRecommendedContainer;
    private ImageView mRecommendedCloseBtn;
    private OnlineUser mChosenUser;
    /**
     * Intent to clicked friend activity
     */
    private OnItemClickListener.OnItemClickCallback itemClickCallback = (view, position) -> {
        Intent openFriend = new Intent(getContext(), FriendProfileActivity.class);
        //Helping fix bug to select needed item after first list loaded
        openFriend.putExtra(Consts.UID, mUserList.get(position).getUid());
        openFriend.putExtra("transitionName", mUserList.get(position).getName());
        startActivity(openFriend);
    };

    /**
     * Intent to chat with clicked friend
     */
    private OnItemClickListener.OnItemClickCallback chatClickCallback = (view, position) -> {
        //Helping fix bug to select needed item after first list loaded
        if (mIsUserExist) {
            mSearchPresenter.openChatOnlineUser(mUserList.get(position).getUid(), mUserList.get(position).getName(), mUserList.get(position).getImage());
        } else {
            Toast.makeText(getContext(), getString(R.string.please_register_to_interact_with_user), Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Set like to clicked friend
     */
    private OnItemClickListener.OnItemClickCallback likeClickCallback = (view, position) -> {
        //Helping fix bug to select needed item after first list loaded
        if (mIsUserExist) {
            mSearchPresenter.setOnlineFriendLiked(mUserList.get(position).getUid());
        } else {
            Toast.makeText(getContext(), getString(R.string.please_register_to_interact_with_user), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStop() {
        super.onStop();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        App app = (App) requireActivity().getApplication();
        app.getComponent().inject(this);
        mPresenter = new OnlineUsersPresenter(new OnlineUsersInteractor(new UserRepository(new Prefs(getContext()))), this);
        mSearchPresenter = new SearchPresenter(new SearchInteractor(new SearchRepository(), new FilterRepository(new Prefs(getContext())), new FriendRepository(new Prefs(getContext())), new RatingRepository(), new UserRepository(new Prefs(getContext())), new HotUsersRepository(new Prefs(getContext()))), new CoinsInteractor(new CoinsRepository()));

        mOnlineUsersRecycler = (RecyclerView) getView().findViewById(R.id.online_users_rv);
        mEmptyUsersTextView = (TextView) getView().findViewById(R.id.online_users_empty_text);
        mBottomButtonContainer = (RelativeLayout) getView().findViewById(R.id.to_see_more_users_container);
        mSeeMoreRegisterButton = (Button) getView().findViewById(R.id.to_see_more_users_button);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.online_users_progress);

        mRecommendedImage = getView().findViewById(R.id.search_recommended_image);
        mRecommendedCloseBtn = getView().findViewById(R.id.search_recommended_close);
        mRecommendedContainer = getView().findViewById(R.id.recommended_container);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        mIsUserExist = mPresenter.isUserExist();
        mPresenter.registerSubscribers();
        layoutManager = new GridLayoutManager(getActivity(), 2);
        mAdapter = new OnlineUsersAdapter(itemClickCallback, chatClickCallback, likeClickCallback);
        mOnlineUsersRecycler.setLayoutManager(layoutManager);
        mOnlineUsersRecycler.setAdapter(mAdapter);
        mPrefs = new Prefs(getContext());
        getLastKeyNode();
        mProgressBar.setVisibility(View.VISIBLE);
        mOnlineUsersRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Pagination works only with registered users
                if (mIsUserExist) {
                    total_item = layoutManager.getItemCount();
                    last_visible_item = layoutManager.findLastVisibleItemPosition();

                    if (!isLoading && total_item <= ((last_visible_item + ITEM_LOAD_COUNT))) {
                        getUsers();
                        isLoading = true;
                    }
                } else {
                    bottomBlockLogic();
                }
            }
        });
        recommendedLogic();
    }

    /**
     * Recommended block logic
     */
    private void recommendedLogic() {
        if (mIsUserExist) {
            mChosenUser = new OnlineUser();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!Consts.RECOMMENDED_SHOWN) {
                        List<OnlineUser> recommendedUsers = new ArrayList<>();
                        for (OnlineUser recommendedUser : mUserList) {
                            if (recommendedUser.getRating() < -1) {
                                recommendedUsers.add(recommendedUser);
                            }
                        }
                        if (recommendedUsers.size() > 0){
                            firebaseAnalytics.logEvent("recommended_shown", null);
                            mRecommendedContainer.setVisibility(View.VISIBLE);
                            Random randomGenerator = new Random();
                            //Getting random user from the recommended user list
                            int index = randomGenerator.nextInt(recommendedUsers.size());
                            mChosenUser = recommendedUsers.get(index);
                            Glide.with(getContext()).load(mChosenUser.getImage()).into(mRecommendedImage);
                            Consts.RECOMMENDED_SHOWN = true;
                        }
                    }
                }
            }, 3000);

            mRecommendedContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseAnalytics.logEvent("recommended_confirmed", null);
                    Intent openFriend = new Intent(getContext(), FriendProfileActivity.class);
                    //Helping fix bug to select needed item after first list loaded
                    openFriend.putExtra(Consts.UID, mChosenUser.getUid());
                    openFriend.putExtra("transitionName", mChosenUser.getName());
                    startActivity(openFriend);
                }
            });
            mRecommendedCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseAnalytics.logEvent("recommended_cancelled", null);
                    mRecommendedContainer.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * Adding real users to the adapter
     *
     * @param onlineUsers
     */
    @Override
    public void addRealUsers(List<OnlineUser> onlineUsers) {
        isLoading = false;
        mUserList.addAll(onlineUsers);
        mAdapter.addUsers(onlineUsers);
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    /**
     * Adding fake users to the list for unregistered user
     *
     * @param onlineUsers
     */
    @Override
    public void addFakeUsers(List<OnlineUser> onlineUsers) {
        mUserList.addAll(onlineUsers);
        mAdapter.addUsers(onlineUsers);
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    private void getUsers() {
        if (!mPrefs.getGender().equals("") && !mPrefs.getGender().equals("default")) {
            if (mIsUserExist) {
                if (!isMaxData) {
                    Query query;
                    if (TextUtils.isEmpty(last_node)) {
                        query = FirebaseDatabase.getInstance().getReference()
                                .child("OnlineUsers")
                                .child(getNeededGender())
                                .orderByChild("rating")
                                .limitToFirst(ITEM_LOAD_COUNT);
                    } else {
                        query = FirebaseDatabase.getInstance().getReference()
                                .child("OnlineUsers")
                                .child(getNeededGender())
                                .orderByChild("rating")
                                .startAt(last_rating, last_node)
                                .limitToFirst(ITEM_LOAD_COUNT);
                    }

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {

                                List<OnlineUser> newUser = new ArrayList<>();
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    newUser.add(userSnapshot.getValue(OnlineUser.class));
                                    last_rating = userSnapshot.getValue(OnlineUser.class).getRating();
                                }
                                last_node = newUser.get(newUser.size() - 1).getUid();

                                if (!last_node.equals(last_key)) {
                                    newUser.remove(newUser.size() - 1);
                                } else {
                                    //Fix error
                                    last_node = "end";
                                    isMaxData = true;
                                }
                                mUserList.addAll(newUser);
                                mAdapter.addUsers(newUser);
                                isLoading = false;
                                mProgressBar.setVisibility(View.GONE);
                            } else {
                                isLoading = false;
                                isMaxData = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            int s = 0;
                        }
                    });
                }
            } else {
                if (!isMaxData) {
                    Query query;
                    if (TextUtils.isEmpty(last_node)) {
                        query = FirebaseDatabase.getInstance().getReference()
                                .child("FakeUsers")
                                .child(getNeededGender())
                                .orderByKey()
                                .limitToFirst(ITEM_LOAD_COUNT);
                    } else {
                        query = FirebaseDatabase.getInstance().getReference()
                                .child("FakeUsers")
                                .child(getNeededGender())
                                .orderByKey()
                                .startAt(last_node)
                                .limitToFirst(ITEM_LOAD_COUNT);
                    }

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {

                                List<OnlineUser> newUser = new ArrayList<>();
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    newUser.add(userSnapshot.getValue(OnlineUser.class));
                                }
                                last_node = newUser.get(newUser.size() - 1).getUid();
                                if (!last_node.equals(last_key)) {
                                    newUser.remove(newUser.size() - 1);
                                } else {
                                    //Fix error
                                    last_node = "end";
                                    isMaxData = true;
                                }
                                mUserList.addAll(newUser);
                                mAdapter.addUsers(newUser);
                                isLoading = false;
                                mProgressBar.setVisibility(View.GONE);
                            } else {
                                isLoading = false;
                                isMaxData = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        } else {
            int x = 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGoneInOnPause) {
            isMaxData = false;
            last_node = mAdapter.getLastItemId();
            mAdapter.removeLastItem();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Clear data to avoid duplicating
        isGoneInOnPause = true;
    }

    /**
     * The logic of getting real users
     * Gets last key first
     * Then make a call to get real users list
     */
    private void getLastKeyNode() {
        if (mIsUserExist) {
            if (!mPrefs.getGender().equals("") && !mPrefs.getGender().equals("default")) {
                Query getLastKey = FirebaseDatabase.getInstance().getReference()
                        .child("OnlineUsers")
                        .child(getNeededGender())
                        .orderByKey()
                        .limitToLast(1);
                getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot lastKey : dataSnapshot.getChildren()) {
                            last_key = lastKey.getKey();
                        }
                        getUsers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Logic", "Last key error " + databaseError.getMessage());
                    }
                });
            }
        } else {
            Query getLastKey = FirebaseDatabase.getInstance().getReference()
                    .child("FakeUsers")
                    .child(getNeededGender())
                    .orderByKey()
                    .limitToLast(1);
            getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot lastKey : dataSnapshot.getChildren()) {
                        last_key = lastKey.getKey();
                    }
                    getUsers();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Logic", "Last key error " + databaseError.getMessage());
                }
            });
        }
    }

    private String getNeededGender() {
        String neededGender;
        if (mPrefs.getGender().equals(Consts.GENDER_FEMALE)) {
            neededGender = Consts.GENDER_MALE;
        } else if (mPrefs.getGender().equals(Consts.GENDER_MALE)) {
            neededGender = Consts.GENDER_FEMALE;
        } else {
            neededGender = Consts.GENDER_FEMALE;
        }
        return neededGender;
    }


    /**
     * Setting last node for pagination
     *
     * @param lastNode - last node value
     */
    @Override
    public void setLastNodeForPagination(String lastNode) {
        last_node = lastNode;
    }

    /**
     * Hiding or showing bottom block for unregistered button
     */
    private void bottomBlockLogic() {
        mBottomButtonContainer.setVisibility(View.GONE);
        int total_item = layoutManager.getItemCount();
        int last_visible_item = layoutManager.findLastVisibleItemPosition();
        //Making bottom registration visible
        boolean endHasBeenReached = last_visible_item + 1 >= total_item;
        if (total_item > 0 && endHasBeenReached) {
            mBottomButtonContainer.setVisibility(View.VISIBLE);
            mBottomButtonContainer.bringToFront();
        }

        mSeeMoreRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainScreenActivity) getActivity()).callAuthWindow();
            }
        });
    }

    /**
     * Triggers when user choose gender first dialog
     *
     * @param gender - chosen gender of the user
     */
    @Override
    public void makeFakeUserCall(String gender) {
        mPresenter.getOnlineFragmentUsers(mIsUserExist);
    }

    /**
     * Open chat with clicked friend
     */
    @Override
    public void openChats(String getmUid, String getmImage, String getmName) {
        Intent chatIntent = new Intent(getContext(), ChatMessageActivity.class);
        chatIntent.putExtra("uid", getmUid);
        chatIntent.putExtra("name", getmName);
        chatIntent.putExtra("photo_url", getmImage);
        startActivity(chatIntent);
    }

    /**
     * Showing registration dialog
     */
    @Override
    public void showRegistrationDialog() {
        getFragmentManager().beginTransaction()
                .add(ConfirmDialogFragment.newInstance(Consts.REG_MODULE), ConfirmDialogFragment.TAG)
                .commit();
    }

    /**
     * Showing full profile dialog
     */
    @Override
    public void showFullProfileDialog() {
        getFragmentManager().beginTransaction()
                .add(ConfirmDialogFragment.newInstance(Consts.FP_MODULE), ConfirmDialogFragment.TAG)
                .commit();
    }

    /**
     * Open slider to let user fill his profile with infortamtion according his account
     *
     * @param list - of of default values for his account to show only needed fields to fill
     */
    @Override
    public void openSlider(ArrayList<String> list) {
        Intent sliderIntent = new Intent(getContext(), SliderActivity.class);
        sliderIntent.putStringArrayListExtra(Consts.DEFAULT_LIST, list);
        startActivity(sliderIntent);
    }


    @Override
    public void setRefreshProgress(boolean progress) {
//        mRefreshSwipeLayout.setRefreshing(progress);
    }

    @Override
    public void onConfirm() {
        mSearchPresenter.openSliderWithDefaultsOnlineUsers();
    }

    @Override
    public void onUpdateDialog() {

    }

    @Override
    public void reviewDialogYes() {

    }

    @Override
    public void reviewDialogNo() {

    }

    @Override
    public void sendToPlayMarket() {

    }

    @Override
    public void sendComplain() {

    }

}
