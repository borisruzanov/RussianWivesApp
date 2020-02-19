package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
        mSearchPresenter = new SearchPresenter(new SearchInteractor(new SearchRepository(), new FilterRepository(new Prefs(getContext())), new FriendRepository(), new RatingRepository(), new UserRepository(new Prefs(getContext())), new HotUsersRepository(new Prefs(getContext()))), new CoinsInteractor(new CoinsRepository()));

        mOnlineUsersRecycler = (RecyclerView) getView().findViewById(R.id.online_users_rv);
        mEmptyUsersTextView = (TextView) getView().findViewById(R.id.online_users_empty_text);
        mBottomButtonContainer = (RelativeLayout) getView().findViewById(R.id.to_see_more_users_container);
        mSeeMoreRegisterButton = (Button) getView().findViewById(R.id.to_see_more_users_button);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.online_users_progress);

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
//                hideBottomBlock();

            }
        });
//        createOfflineDBWomen();

//        createOfflineDBWomen();
//        createOfflineDBMen();
    }

//    /**
//     * Getting user block
//     */
//    private void getUsers() {
//        Prefs prefs = new Prefs(getContext());
//        total_item = layoutManager.getItemCount();
//        last_visible_item = layoutManager.findLastVisibleItemPosition();
//        //Call if gender is not empty
//        if (!prefs.getGender().equals("") && !prefs.getGender().equals("default")) {
//            mPresenter.getOnlineFragmentUsers(mIsUserExist);
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//    }


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
        int s = 0;
        s += onlineUsers.size();
        Log.d("zzzz", "" + s);

    }

    private void removeDuplicates(List<OnlineUser> onlineUsers) {
        for (OnlineUser databaseUser : onlineUsers) {
            for (OnlineUser localUser : mUserList) {
                //Checking if new received data already been added to the local list
                if (localUser.hashCode() == databaseUser.hashCode()) {
                    filteredList.add(databaseUser);
                }
            }
        }
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


    private void createOfflineDBWomen() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference poemsCatRefRus = database.getReference().child("FakeUsers").child("Male");
        poemsCatRefRus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference poemsCatRefRusz = database.getReference().child("OnlineUsers").child("Male");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    OnlineUser removeUser = postSnapshot.getValue(OnlineUser.class);
                    poemsCatRefRusz.child(removeUser.getUid()).setValue(removeUser);
                }

//                List<OnlineUser> menOfflineList = new ArrayList<>();
//                menOfflineList.add(new OnlineUser("dqLyIncyH9hxUkb3iGVWwFOmnI03", "Smith Durat", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FdqLyIncyH9hxUkb3iGVWwFOmnI03%2Fprofile_photo?alt=media&token=074b36fb-9cef-4d43-b09c-b4b21b0bbcc6", "Male", "Australia", -1));
//                menOfflineList.add(new OnlineUser("JmrPxXMPEOgnAzbwNRkk3q4xmdB2", "Bob Steel", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FJmrPxXMPEOgnAzbwNRkk3q4xmdB2%2Fprofile_photo?alt=media&token=b772a8be-c2df-46cb-9075-d1529bbbae96", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("erQDqO5vDNWGzUWCtqakFv5KPt53", "John Seever", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FerQDqO5vDNWGzUWCtqakFv5KPt53%2Fprofile_photo?alt=media&token=8161b72e-9fcd-4816-bd66-1d414a49adee", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("CsZGlZsWVJbBQBbRwVbGfaOtJ4C3", "Huan Nacho", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FCsZGlZsWVJbBQBbRwVbGfaOtJ4C3%2Fprofile_photo?alt=media&token=4a325738-5dd2-42a5-b54b-e40218bb78f4", "Male", "Brazil", -1));
//                menOfflineList.add(new OnlineUser("G66g3i8piWgL0RK7NHndovvRvgn2", "Chris J", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FG66g3i8piWgL0RK7NHndovvRvgn2%2Fprofile_photo?alt=media&token=f250877c-48a6-49e7-92ba-28597e381955", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("Ivc9MBYEGoRyq3hcmoYdMtdWj6l1", "Joel Sanz", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FIvc9MBYEGoRyq3hcmoYdMtdWj6l1%2Fprofile_photo?alt=media&token=f303495d-87c4-41ab-a561-88b46d8854c3", "Male", "Austria", -1));
//                menOfflineList.add(new OnlineUser("Wp9O01652aMUQKFoZCN5fjVzNHj2", "Smith Thomas", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FWp9O01652aMUQKFoZCN5fjVzNHj2%2Fprofile_photo?alt=media&token=4f122932-dc51-423a-8685-3a52590cb397", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("emGVmCwGGIb7dyrbp88fbWtnT0S2", "Rob Nid", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FemGVmCwGGIb7dyrbp88fbWtnT0S2%2Fprofile_photo?alt=media&token=fc98bdd7-7a46-493c-8328-3fcf1d555851", "Male", "Canada", -1));
//                menOfflineList.add(new OnlineUser("gCHU8G4EfPS4uSHR4Y45BbI57wd2", "Juan Chandez", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FgCHU8G4EfPS4uSHR4Y45BbI57wd2%2Fprofile_photo?alt=media&token=7b3e15de-ada3-4926-bc44-17cfc946c48e", "Male", "Spain", -1));
//                menOfflineList.add(new OnlineUser("gITeIX5H4AaaAPk4z8IRxASL4lO2", "Andy Peres", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FgITeIX5H4AaaAPk4z8IRxASL4lO2%2Fprofile_photo?alt=media&token=048b1fac-c6ed-41e4-ba15-6efb2e47b796", "Male", "Argentina", -1));
//                menOfflineList.add(new OnlineUser("pbtf1yiLwpOwLJ9Y6R7J0MFvt7C2", "David Fitch", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fpbtf1yiLwpOwLJ9Y6R7J0MFvt7C2%2Fprofile_photo?alt=media&token=df71f4bd-11c0-489b-b24f-4465da6d51ff", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("tBVa8DDfbsXA4DQ4waUDY89Db8n1", "Jeremy Stevens", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FtBVa8DDfbsXA4DQ4waUDY89Db8n1%2Fprofile_photo?alt=media&token=0d5fe67a-de8e-4522-b744-2c2ddb9ad121", "Male", "Canada", -1));
//                menOfflineList.add(new OnlineUser("7E0i7WHhwXgLASgKm0oyp29spH32", "Lucas S", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F7E0i7WHhwXgLASgKm0oyp29spH32%2Fprofile_photo?alt=media&token=da0caada-2759-40fd-9fcf-a2922fcd1ff5", "Male", "Denmark", -1));
//                menOfflineList.add(new OnlineUser("OdY1bZ1FBxcOct4FrXu0qlwXWvG3", "Paul Debrice", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FOdY1bZ1FBxcOct4FrXu0qlwXWvG3%2Fprofile_photo?alt=media&token=217a9589-a15d-448a-9909-a3e68bb6a7cc", "Male", "France", -1));
//                menOfflineList.add(new OnlineUser("p2GiE92UzwarYlQ01JoK4Kapj3D3", "Mike Smith", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fp2GiE92UzwarYlQ01JoK4Kapj3D3%2Fprofile_photo?alt=media&token=0b45e492-16c2-4476-a682-4b3e68b29b04", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("rgbwKQnDsZSXroDcwqBX7u2qNNI2", "Mark Hetcher", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FrgbwKQnDsZSXroDcwqBX7u2qNNI2%2Fprofile_photo?alt=media&token=c207c0f4-8abd-41c1-9ef1-d5c32ebd29fb", "Male", "Canada", -1));
//                menOfflineList.add(new OnlineUser("uSkQXfO4yBhpfojNHt9pTjxe0Iw2", "Sanchez Juano", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FuSkQXfO4yBhpfojNHt9pTjxe0Iw2%2Fprofile_photo?alt=media&token=e4ca6781-f18d-492b-9205-6c3b768402b6", "Male", "Mexico", -1));
//                menOfflineList.add(new OnlineUser("3uuDS6mm1nOPhhBDKIVg70CMbr82", "Jerard Dutue", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F3uuDS6mm1nOPhhBDKIVg70CMbr82%2Fprofile_photo?alt=media&token=6777bfe0-3551-4546-afea-81f29cdd8bf4", "Male", "France", -1));
//                menOfflineList.add(new OnlineUser("BshwjhRKHwULoPTI0pRZGu1po2q1", "John W", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FBshwjhRKHwULoPTI0pRZGu1po2q1%2Fprofile_photo?alt=media&token=516ec778-0d08-457e-93b0-e02a921bc430", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("PY8IVYwd3BYWFQlEYUuzLnzI1M32", "Leo Bonart", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FPY8IVYwd3BYWFQlEYUuzLnzI1M32%2Fprofile_photo?alt=media&token=90ed7ddb-dd25-4438-8b61-4da37e5a2fcd", "Male", "Austria", -1));
//                menOfflineList.add(new OnlineUser("xDmdhY49nrYCt12OvccIMH1UBwr2", "Chris White", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FxDmdhY49nrYCt12OvccIMH1UBwr2%2Fprofile_photo?alt=media&token=ed295527-6e25-4219-88ff-a9720b234803", "Male", "USA", -1));
//                menOfflineList.add(new OnlineUser("xQO5KSxiXidWZirA2ZRjPe5TMiF3", "Hern Hurtan", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FxQO5KSxiXidWZirA2ZRjPe5TMiF3%2Fprofile_photo?alt=media&token=8c8a89e6-907c-42df-be23-70978940c8b2", "Male", "Germany", -1));
//                menOfflineList.add(new OnlineUser("8IEopEX1j9T9TCbKGJ3rwWXyaJT2", "Sergio Juan", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F8IEopEX1j9T9TCbKGJ3rwWXyaJT2%2Fprofile_photo?alt=media&token=f89f554b-6abe-4b19-857b-b08fddded245", "Male", "Brazil", -1));
//                menOfflineList.add(new OnlineUser("QxP0kLw4WVVUh0qjSNFxMpur56v2", "Swet Karny", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FQxP0kLw4WVVUh0qjSNFxMpur56v2%2Fprofile_photo?alt=media&token=e08f38c4-5fda-46c1-a16b-3608e7be643a", "Male", "Sweden", -1));
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    OnlineUser removeUser = postSnapshot.getValue(OnlineUser.class);
//                    if (removeUser.getCountry().equals("Pakistan") || removeUser.getCountry().equals("Tajikistan")) {
//                        postSnapshot.getRef().removeValue();
//                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//        List<OnlineUser> menOfflineList = new ArrayList<>();
//        menOfflineList.add(new OnlineUser("3KTM8ZjHYEgnxXRqi9WEDeG9bXq1", "Sveta Birova", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F3KTM8ZjHYEgnxXRqi9WEDeG9bXq1%2Fprofile_photo?alt=media&token=ac3c6ba7-59a9-4884-8928-8d9dd0d3e1a5", "Female", "Belarus", -1));
//        menOfflineList.add(new OnlineUser("BiTT0ITOMPctRJAYH7GzqpyIuau2", "Ira Berezina", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FBiTT0ITOMPctRJAYH7GzqpyIuau2%2Fprofile_photo?alt=media&token=9fa6bbb4-05c3-4ae3-a0e9-a467e417a3ab", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("IbvmKd5upTNyDxr9sCT0RgIl2Rt1", "Suzanna L", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FIbvmKd5upTNyDxr9sCT0RgIl2Rt1%2Fprofile_photo?alt=media&token=5ecbe716-0e55-4f50-b445-f7b64f649c08", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("Wuq70dyfadZUH2fiDB8OIwPfnHW2", "Gavanah S", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FWuq70dyfadZUH2fiDB8OIwPfnHW2%2Fprofile_photo?alt=media&token=2390a5bc-f4cf-49da-840b-5b61e03bd733", "Female", "Belarus", -1));
//        menOfflineList.add(new OnlineUser("qW9YVFIYsqWUhfSqXE66uS4vrzw2", "Anna Pileva", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FqW9YVFIYsqWUhfSqXE66uS4vrzw2%2Fprofile_photo?alt=media&token=15d1949a-b1bd-4a9d-a2a8-3968874da70d", "Female", "Ukraine", -1));
//        menOfflineList.add(new OnlineUser("u69Q5xkDYEhPlPUv8LDQhuuo51l1", "Kris N", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fu69Q5xkDYEhPlPUv8LDQhuuo51l1%2Fprofile_photo?alt=media&token=fcdca353-03d8-4a4c-8617-cc5993ba87db", "Female", "Ukraine", -1));
//        menOfflineList.add(new OnlineUser("zkzqgcy7mBgkoprHlH4TNKAINqm2", "Gulmira K", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fzkzqgcy7mBgkoprHlH4TNKAINqm2%2Fprofile_photo?alt=media&token=df1a351a-0270-40f3-926e-85d4d727fde0", "Female", "Kazakhstan", -1));
//        menOfflineList.add(new OnlineUser("zmhdo8JqCaOzGN80L6WdiBS6r3p2", "Yulia Grib", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fzmhdo8JqCaOzGN80L6WdiBS6r3p2%2Fprofile_photo?alt=media&token=ea41e591-5572-4180-872c-4b8520d21886", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("CBkrfQgwrucX3O0CP41ixiYB5vx2", "Angella Z", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FCBkrfQgwrucX3O0CP41ixiYB5vx2%2Fprofile_photo?alt=media&token=370400b3-535c-4cc5-bc9e-9d5d522f705e", "Female", "Ukraine", -1));
//        menOfflineList.add(new OnlineUser("KNFOdnxZy7Zuu5Ph6n5uronoCMt2", "Jane J", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FKNFOdnxZy7Zuu5Ph6n5uronoCMt2%2Fprofile_photo?alt=media&token=28b20f48-bc5c-4e38-8e18-f7b6fa591999", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("Tzu28eDvtpPbSsC6D0G65oznEfZ2", "Fiya M", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FTzu28eDvtpPbSsC6D0G65oznEfZ2%2Fprofile_photo?alt=media&token=e865b5d1-7817-448c-830a-96501a1156ea", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("nIPxH19NyuelfesrQnL6RJf0swo2", "Daria C", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FnIPxH19NyuelfesrQnL6RJf0swo2%2Fprofile_photo?alt=media&token=db1c835e-83a2-4842-b87b-5b8ea0ffe075", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("ZJ2TkG7B2bhvIcWIL93iC3mcDIq1", "Ira M", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FZJ2TkG7B2bhvIcWIL93iC3mcDIq1%2Fprofile_photo?alt=media&token=137799b5-be2c-4fc6-8328-1fa3c91654a1", "Female", "Belarus", -1));
//        menOfflineList.add(new OnlineUser("pVb3Vstu6OXxGj8nCIGiNzGElcZ2", "Lena K", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FpVb3Vstu6OXxGj8nCIGiNzGElcZ2%2Fprofile_photo?alt=media&token=7a848d64-0ee1-4807-87a2-6d9e3d5025c3", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("qe7Gxkn4gEVKsp3PMDxbULBBb7O2", "Olya J", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fqe7Gxkn4gEVKsp3PMDxbULBBb7O2%2Fprofile_photo?alt=media&token=a52a26c9-10a1-4973-ab09-8d53e04eb203", "Female", "Ukraine", -1));
//        menOfflineList.add(new OnlineUser("tV6iGKujx8R24OPJKjy3ndc5L5i2", "Irina Dud", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FtV6iGKujx8R24OPJKjy3ndc5L5i2%2Fprofile_photo?alt=media&token=aaeac377-69dc-400e-b939-8899edeb0f50", "Female", "Ukraine", -1));
//        menOfflineList.add(new OnlineUser("wvoJjSUAsgRERQC9YO86XmFtQ4m1", "Gul Giova", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FwvoJjSUAsgRERQC9YO86XmFtQ4m1%2Fprofile_photo?alt=media&token=97350de1-7295-4cfc-aa39-b63e6b24d798", "Female", "Kazakhstan", -1));
//        menOfflineList.add(new OnlineUser("yiP3oLvWkiVWRsgKlelyF9rkRok2", "Liya O", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FyiP3oLvWkiVWRsgKlelyF9rkRok2%2Fprofile_photo?alt=media&token=bec9f709-56ff-4c7f-aae9-10182dcd5e90", "Female", "Ukraine", -1));
//        menOfflineList.add(new OnlineUser("2XQTHt5li2eIcgs3klvkR1N0iqa2", "Varvara O", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F2XQTHt5li2eIcgs3klvkR1N0iqa2%2Fprofile_photo?alt=media&token=e5ca34d9-efe6-456c-85a9-334cb81b7c0b", "Female", "Ukraine", -1));
//        menOfflineList.add(new OnlineUser("6kqEr0vg3cPJNfeftTAVV5UXsVX2", "Yulia Belova", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F6kqEr0vg3cPJNfeftTAVV5UXsVX2%2Fprofile_photo?alt=media&token=a240759a-21e1-40db-9242-cbc29413f7eb", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("83jH7W3bX8OPk6V2442uPxfsVHm1", "Sveta Cherkina", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F83jH7W3bX8OPk6V2442uPxfsVHm1%2Fprofile_photo?alt=media&token=86cae365-21ad-4e21-a5e5-03856fb92b00", "Female", "Russia", -1));
//        menOfflineList.add(new OnlineUser("HQd4EL8y34QJskEj6n41QiuYK822", "Karina Gogol", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FHQd4EL8y34QJskEj6n41QiuYK822%2Fprofile_photo?alt=media&token=bf67b473-7b3f-4eed-abf1-8c4470d81675", "Female", "Kazakhstan", -1));
//        menOfflineList.add(new OnlineUser("LCymSUf3VgeWDHSABbmxA0cP7JS2", "Kris Ten", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FLCymSUf3VgeWDHSABbmxA0cP7JS2%2Fprofile_photo?alt=media&token=b7ff3917-1710-4a48-a6fd-dfc13055757c", "Female", "Georgia", -1));
//        menOfflineList.add(new OnlineUser("fxQLgeFuJ8VdL7UPR1T6n0wsiUF3", "Alena Berkel", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FfxQLgeFuJ8VdL7UPR1T6n0wsiUF3%2Fprofile_photo?alt=media&token=94c336b0-819f-434f-a695-cdf1d64467e6", "Female", "Bulgaria", -1));
//        menOfflineList.add(new OnlineUser("idcbhxoKvAeukJzebSocIvfk4YA2", "Darya Litvenko", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FidcbhxoKvAeukJzebSocIvfk4YA2%2Fprofile_photo?alt=media&token=28385cfc-77ec-48bf-a2b7-ae6b3d685cfc", "Female", "Belarus", -1));

//        for (OnlineUser user : menOfflineList) {
//            poemsCatRefRus.child(user.getUid()).setValue(user);
//        }
    }

    private void createOfflineDBMen() {
        CollectionReference users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);
        users
                .whereEqualTo("full_profile", "true")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (snapshot.getString(Consts.GENDER).equals(Consts.FEMALE)) {
                                    if (snapshot.getString(Consts.COUNTRY).equals("Russia") || snapshot.getString(Consts.COUNTRY).equals("Ukraine") || snapshot.getString(Consts.COUNTRY).equals("Kazakhstan") || snapshot.getString(Consts.COUNTRY).equals("Belarus")) {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference poemsCatRefRus = database.getReference().child("FakeUsers").child("Female");
                                        OnlineUser user = new OnlineUser();
                                        user.setUid(snapshot.getString(Consts.UID));
                                        user.setRating(-1L);
                                        user.setName(snapshot.getString(Consts.NAME));
                                        user.setImage(snapshot.getString(Consts.IMAGE));
                                        user.setGender(snapshot.getString(Consts.GENDER));
                                        user.setCountry(snapshot.getString(Consts.COUNTRY));
                                        poemsCatRefRus.child(user.getUid()).setValue(user);
                                    }
                                } else if (snapshot.getString(Consts.GENDER).equals(Consts.MALE)) {
//                                    if (!snapshot.getString(Consts.COUNTRY).equals("Russia") && !snapshot.getString(Consts.COUNTRY).equals("Ukraine") && !snapshot.getString(Consts.COUNTRY).equals("Kazakhstan") && !snapshot.getString(Consts.COUNTRY).equals("Afghanistan") && !snapshot.getString(Consts.COUNTRY).equals("Belarus") && !snapshot.getString(Consts.COUNTRY).equals("India") && !snapshot.getString(Consts.COUNTRY).equals("default")) {
//                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                        DatabaseReference poemsCatRefRus = database.getReference().child("FakeUsers").child("Male");
//                                        OnlineUser user = new OnlineUser();
//                                        user.setUid(snapshot.getString(Consts.UID));
//                                        user.setRating(-1L);
//                                        user.setName(snapshot.getString(Consts.NAME));
//                                        user.setImage(snapshot.getString(Consts.IMAGE));
//                                        user.setGender(snapshot.getString(Consts.GENDER));
//                                        user.setCountry(snapshot.getString(Consts.COUNTRY));
//                                        poemsCatRefRus.child(user.getUid()).setValue(user);
//                                    }
                                }

                            }
                        } else {
                        }
                    }
                });

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference poemsCatRefRus = database.getReference().child("FakeUsers").child("Male");
        List<OnlineUser> menOfflineList = new ArrayList<>();
        menOfflineList.add(new OnlineUser("dqLyIncyH9hxUkb3iGVWwFOmnI03", "Smith Durat", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FdqLyIncyH9hxUkb3iGVWwFOmnI03%2Fprofile_photo?alt=media&token=074b36fb-9cef-4d43-b09c-b4b21b0bbcc6", "Male", "Australia", -1));
        menOfflineList.add(new OnlineUser("JmrPxXMPEOgnAzbwNRkk3q4xmdB2", "Bob Steel", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FJmrPxXMPEOgnAzbwNRkk3q4xmdB2%2Fprofile_photo?alt=media&token=b772a8be-c2df-46cb-9075-d1529bbbae96", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("erQDqO5vDNWGzUWCtqakFv5KPt53", "John Seever", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FerQDqO5vDNWGzUWCtqakFv5KPt53%2Fprofile_photo?alt=media&token=8161b72e-9fcd-4816-bd66-1d414a49adee", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("CsZGlZsWVJbBQBbRwVbGfaOtJ4C3", "Huan Nacho", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FCsZGlZsWVJbBQBbRwVbGfaOtJ4C3%2Fprofile_photo?alt=media&token=4a325738-5dd2-42a5-b54b-e40218bb78f4", "Male", "Brazil", -1));
        menOfflineList.add(new OnlineUser("G66g3i8piWgL0RK7NHndovvRvgn2", "Chris J", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FG66g3i8piWgL0RK7NHndovvRvgn2%2Fprofile_photo?alt=media&token=f250877c-48a6-49e7-92ba-28597e381955", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("Ivc9MBYEGoRyq3hcmoYdMtdWj6l1", "Joel Sanz", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FIvc9MBYEGoRyq3hcmoYdMtdWj6l1%2Fprofile_photo?alt=media&token=f303495d-87c4-41ab-a561-88b46d8854c3", "Male", "Austria", -1));
        menOfflineList.add(new OnlineUser("Wp9O01652aMUQKFoZCN5fjVzNHj2", "Smith Thomas", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FWp9O01652aMUQKFoZCN5fjVzNHj2%2Fprofile_photo?alt=media&token=4f122932-dc51-423a-8685-3a52590cb397", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("emGVmCwGGIb7dyrbp88fbWtnT0S2", "Rob Nid", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FemGVmCwGGIb7dyrbp88fbWtnT0S2%2Fprofile_photo?alt=media&token=fc98bdd7-7a46-493c-8328-3fcf1d555851", "Male", "Canada", -1));
        menOfflineList.add(new OnlineUser("gCHU8G4EfPS4uSHR4Y45BbI57wd2", "Juan Chandez", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FgCHU8G4EfPS4uSHR4Y45BbI57wd2%2Fprofile_photo?alt=media&token=7b3e15de-ada3-4926-bc44-17cfc946c48e", "Male", "Spain", -1));
        menOfflineList.add(new OnlineUser("gITeIX5H4AaaAPk4z8IRxASL4lO2", "Andy Peres", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FgITeIX5H4AaaAPk4z8IRxASL4lO2%2Fprofile_photo?alt=media&token=048b1fac-c6ed-41e4-ba15-6efb2e47b796", "Male", "Argentina", -1));
        menOfflineList.add(new OnlineUser("pbtf1yiLwpOwLJ9Y6R7J0MFvt7C2", "David Fitch", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fpbtf1yiLwpOwLJ9Y6R7J0MFvt7C2%2Fprofile_photo?alt=media&token=df71f4bd-11c0-489b-b24f-4465da6d51ff", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("tBVa8DDfbsXA4DQ4waUDY89Db8n1", "Jeremy Stevens", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FtBVa8DDfbsXA4DQ4waUDY89Db8n1%2Fprofile_photo?alt=media&token=0d5fe67a-de8e-4522-b744-2c2ddb9ad121", "Male", "Canada", -1));
        menOfflineList.add(new OnlineUser("7E0i7WHhwXgLASgKm0oyp29spH32", "Lucas S", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F7E0i7WHhwXgLASgKm0oyp29spH32%2Fprofile_photo?alt=media&token=da0caada-2759-40fd-9fcf-a2922fcd1ff5", "Male", "Denmark", -1));
        menOfflineList.add(new OnlineUser("OdY1bZ1FBxcOct4FrXu0qlwXWvG3", "Paul Debrice", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FOdY1bZ1FBxcOct4FrXu0qlwXWvG3%2Fprofile_photo?alt=media&token=217a9589-a15d-448a-9909-a3e68bb6a7cc", "Male", "France", -1));
        menOfflineList.add(new OnlineUser("p2GiE92UzwarYlQ01JoK4Kapj3D3", "Mike Smith", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fp2GiE92UzwarYlQ01JoK4Kapj3D3%2Fprofile_photo?alt=media&token=0b45e492-16c2-4476-a682-4b3e68b29b04", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("rgbwKQnDsZSXroDcwqBX7u2qNNI2", "Mark Hetcher", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FrgbwKQnDsZSXroDcwqBX7u2qNNI2%2Fprofile_photo?alt=media&token=c207c0f4-8abd-41c1-9ef1-d5c32ebd29fb", "Male", "Canada", -1));
        menOfflineList.add(new OnlineUser("uSkQXfO4yBhpfojNHt9pTjxe0Iw2", "Sanchez Juano", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FuSkQXfO4yBhpfojNHt9pTjxe0Iw2%2Fprofile_photo?alt=media&token=e4ca6781-f18d-492b-9205-6c3b768402b6", "Male", "Mexico", -1));
        menOfflineList.add(new OnlineUser("3uuDS6mm1nOPhhBDKIVg70CMbr82", "Jerard Dutue", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F3uuDS6mm1nOPhhBDKIVg70CMbr82%2Fprofile_photo?alt=media&token=6777bfe0-3551-4546-afea-81f29cdd8bf4", "Male", "France", -1));
        menOfflineList.add(new OnlineUser("BshwjhRKHwULoPTI0pRZGu1po2q1", "John W", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FBshwjhRKHwULoPTI0pRZGu1po2q1%2Fprofile_photo?alt=media&token=516ec778-0d08-457e-93b0-e02a921bc430", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("PY8IVYwd3BYWFQlEYUuzLnzI1M32", "Leo Bonart", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FPY8IVYwd3BYWFQlEYUuzLnzI1M32%2Fprofile_photo?alt=media&token=90ed7ddb-dd25-4438-8b61-4da37e5a2fcd", "Male", "Austria", -1));
        menOfflineList.add(new OnlineUser("xDmdhY49nrYCt12OvccIMH1UBwr2", "Chris White", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FxDmdhY49nrYCt12OvccIMH1UBwr2%2Fprofile_photo?alt=media&token=ed295527-6e25-4219-88ff-a9720b234803", "Male", "USA", -1));
        menOfflineList.add(new OnlineUser("xQO5KSxiXidWZirA2ZRjPe5TMiF3", "Hern Hurtan", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FxQO5KSxiXidWZirA2ZRjPe5TMiF3%2Fprofile_photo?alt=media&token=8c8a89e6-907c-42df-be23-70978940c8b2", "Male", "Germany", -1));
        menOfflineList.add(new OnlineUser("8IEopEX1j9T9TCbKGJ3rwWXyaJT2", "Sergio Juan", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F8IEopEX1j9T9TCbKGJ3rwWXyaJT2%2Fprofile_photo?alt=media&token=f89f554b-6abe-4b19-857b-b08fddded245", "Male", "Brazil", -1));
        menOfflineList.add(new OnlineUser("QxP0kLw4WVVUh0qjSNFxMpur56v2", "Swet Karny", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FQxP0kLw4WVVUh0qjSNFxMpur56v2%2Fprofile_photo?alt=media&token=e08f38c4-5fda-46c1-a16b-3608e7be643a", "Male", "Sweden", -1));


        //        menOfflineList.add(new OnlineUser("SpGE02sAIHgw4fEewNzdysQMnWB3", "Jo Herkel", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FSpGE02sAIHgw4fEewNzdysQMnWB3%2Fprofile_photo?alt=media&token=bd17d90c-d113-453a-baf5-7357132d3083", "Male", "Germany", 1));
//        menOfflineList.add(new OnlineUser("djf6ZSfrIfXjdlWefmXWwnl8Fdx1", "Miguel Sancho", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fdjf6ZSfrIfXjdlWefmXWwnl8Fdx1%2Fprofile_photo?alt=media&token=9ec3143b-a24b-4a02-a51d-f43e77e8bb4f", "Male", "Spain", 1));
//        menOfflineList.add(new OnlineUser("qgrecp4SnrYgAV5Zf3JLxItyv5J2", "Cisse Jerdi", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fqgrecp4SnrYgAV5Zf3JLxItyv5J2%2Fprofile_photo?alt=media&token=8a7d28fc-8f91-4dab-bbec-2543e36c5c0f", "Male", "France", 1));
//        menOfflineList.add(new OnlineUser("0CYSTfDVa3QCBKoht9YBzWqOAb82", "Jorhe Jimdo", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F0CYSTfDVa3QCBKoht9YBzWqOAb82%2Fprofile_photo?alt=media&token=4552e8c9-8c80-4766-8f2f-995913ced979", "Male", "Brazil", 1));
//        menOfflineList.add(new OnlineUser("4R87rgnKTCU4k5tdTHFEmo0xxKi2", "Jimmy J", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F4R87rgnKTCU4k5tdTHFEmo0xxKi2%2Fprofile_photo?alt=media&token=15d41e50-9fa0-44fd-8011-3c7e7b02b957", "Male", "USA", 1));
//        menOfflineList.add(new OnlineUser("4kJtUi5JG1MszpvzIZmLQagY8zE3", "Colin Texy", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F4kJtUi5JG1MszpvzIZmLQagY8zE3%2Fprofile_photo?alt=media&token=8be0c96c-7188-4df7-bdfa-1113bdd1d40c", "Male", "Canada", 1));
//        menOfflineList.add(new OnlineUser("IUxV7joi30e20sOndxAyXNrKlw82", "Giro Pindelli", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FIUxV7joi30e20sOndxAyXNrKlw82%2Fprofile_photo?alt=media&token=e04d627c-51a0-4b6f-95d7-5a50edaad16b", "Male", "Italy", 1));
//        menOfflineList.add(new OnlineUser("oJKBCULkteedR3JKu1hFWaBK9uU2", "Sebastian Severn", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FoJKBCULkteedR3JKu1hFWaBK9uU2%2Fprofile_photo?alt=media&token=e965663b-f258-4fc7-81f4-1c7c5178810b", "Male", "USA", 1));
//        menOfflineList.add(new OnlineUser("9f699FD0dYb576jg6O48I26ugh02", "Guano Niche", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2F9f699FD0dYb576jg6O48I26ugh02%2Fprofile_photo?alt=media&token=cc59cab9-f40a-4c55-88be-e13b8b51d752", "Male", "Italy", 1));
//        menOfflineList.add(new OnlineUser("aTcqqb5HatOPUfVdlcGbQyPut5f1", "Andrew Kirek", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FaTcqqb5HatOPUfVdlcGbQyPut5f1%2Fprofile_photo?alt=media&token=337844ef-3d01-47a6-af03-4cbb1cde34f3", "Male", "Canada", 1));
//        menOfflineList.add(new OnlineUser("mfxN3sUvZldcplmg00D5m6pRfCh1", "Rob B", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FmfxN3sUvZldcplmg00D5m6pRfCh1%2Fprofile_photo?alt=media&token=3b5c4add-720d-44c8-a6f5-dc4f95dadfe0", "Male", "Austria", 1));
//        menOfflineList.add(new OnlineUser("s55US23UOkhKGFbamxoMa1yl5IL2", "Bill Sup", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2Fs55US23UOkhKGFbamxoMa1yl5IL2%2Fprofile_photo?alt=media&token=4e722ec4-263e-4f9a-a603-601cc384f3da", "Male", "USA", 1));
//        menOfflineList.add(new OnlineUser("uG54uC3dvkNWgiz39M1lkYLNlEY2", "Pablo B", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FuG54uC3dvkNWgiz39M1lkYLNlEY2%2Fprofile_photo?alt=media&token=0de77e65-c951-4201-a961-18f3a19cd36c", "Male", "Brazil", 1));

//        for (OnlineUser user : menOfflineList) {
//            poemsCatRefRus.child(user.getUid()).setValue(user);
//        }
    }


}
