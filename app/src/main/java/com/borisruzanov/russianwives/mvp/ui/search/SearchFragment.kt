package com.borisruzanov.russianwives.mvp.ui.search


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.models.HotUser
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity
import com.borisruzanov.russianwives.mvp.ui.purchasedialog.PurchaseDialogFragment
import com.borisruzanov.russianwives.mvp.ui.rewardvideo.RewardVideoActivity
import com.borisruzanov.russianwives.mvp.ui.search.adapter.FeedScrollListener
import com.borisruzanov.russianwives.mvp.ui.search.adapter.SearchAdapter
import com.borisruzanov.russianwives.mvp.ui.hots.HotAdapter
import com.borisruzanov.russianwives.mvp.ui.hots.InfiniteScrollListener
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity
import com.borisruzanov.russianwives.utils.Consts
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_main_tab_search.*
import java.util.ArrayList
import java.util.Objects

import javax.inject.Inject

class SearchFragment : MvpAppCompatFragment(), SearchView, ConfirmDialogFragment.ConfirmListener,
        PurchaseDialogFragment.ConfirmPurchaseListener, HotAdapter.ItemClickListener,
        InfiniteScrollListener.OnLoadMoreListener {

    @Inject
    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter
    var item: MenuItem? = null
    @ProvidePresenter
    fun providePresenter() = searchPresenter

    private lateinit var layoutManager: GridLayoutManager
    private var dialogFragment = FilterDialogFragment()
    private var infiniteScrollListener: InfiniteScrollListener? = null

    private lateinit var hotAdapter: HotAdapter

    private val onItemChatCallback = { _: View, position: Int ->
        firebaseAnalytics.logEvent("start_chat_from_main_search", null)
        searchPresenter.openChat(position)
    }

    private val onItemLikeCallback = { _: View, position: Int ->
        firebaseAnalytics.logEvent("like_from_main_search", null)
        searchPresenter.setFriendLiked(position)
    }

    private val onItemClickCallback = { view: View, position: Int ->
        val sharedImageView = view as ImageView
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                Objects.requireNonNull<FragmentActivity>(activity), sharedImageView, ViewCompat.getTransitionName(sharedImageView)!!)

        searchPresenter.openFriend(position, Objects.requireNonNull<Bundle>(options.toBundle()))
    }

    override fun onItemClick(view: View?, position: Int) {
        if (position == 0) searchPresenter.purchaseHot()
        Toast.makeText(activity, "You clicked on item with position $position", Toast.LENGTH_SHORT).show()
    }

    lateinit var firebaseAnalytics: FirebaseAnalytics

    private var adapter = SearchAdapter(onItemClickCallback, onItemChatCallback, onItemLikeCallback)

    private lateinit var onUserListScrollListener: FeedScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        // retainInstance = true
        val application = requireActivity().application as App
        application.component.inject(this)
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_tab_search, container, false)
    }
    private var visible: Boolean = false

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        if (menuVisible) {
            Log.d("xxx","Visible true")
            visible = true
        } else{
        Log.d("xxx","Visible false")}
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        if (visible) {
            if (menu != null) {
                menu.findItem(R.id.shop).isVisible = true
            }
        } else {
            if (menu != null) {
            }
        }   
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // set up the RecyclerView
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        infiniteScrollListener = InfiniteScrollListener(horizontalLayoutManager, this)
        users_hot_recycler.layoutManager = horizontalLayoutManager
        users_hot_recycler.addOnScrollListener(infiniteScrollListener!!)
        hotAdapter = HotAdapter()
        hotAdapter.setClickListener(this)
        users_hot_recycler.adapter = hotAdapter
        searchPresenter.getHotUsersByPage()

        users_hot_recycler.setOnClickListener {
            Log.d("qwe", "show filter dialog")
            dialogFragment.show(activity?.supportFragmentManager, FilterDialogFragment.TAG)
        }

        layoutManager = GridLayoutManager(activity, 3)

        onUserListScrollListener = object : FeedScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d("UsersListDebug", "in onUserScrollListener and page is $page")
                searchPresenter.setProgressBar(true)
                searchPresenter.getUserList(page)
            }
        }

        if (users_recycler_view.layoutManager == null) {
            users_recycler_view.layoutManager = layoutManager
        }

        users_recycler_view.setHasFixedSize(true)
        users_recycler_view.adapter = adapter
        users_recycler_view.addOnScrollListener(onUserListScrollListener)

        users_swipe_refresh.setOnRefreshListener {
            Log.d("UsersListDebug", "refreshing ...")
            onUpdate()
        }

        searchPresenter.setProgressBar(true)
        searchPresenter.getUserList(0)
    }

    override fun addUsers(userList: List<FsUser>) {
        searchPresenter.setProgressBar(false)
        Log.d("UsersListDebug", "Stopped loading is ${onUserListScrollListener.isStoppedLoading}")
        //!important! when using FeedScrollListener we need manually tell it about the end of the list
        if (userList.size == 1) {
            onUserListScrollListener.setStopLoading(true)
            Log.d("UsersListDebug", "Stop loading")
        }
        adapter.addUsers(userList)
    }

    override fun setProgressBar(isLoading: Boolean) {
        users_swipe_refresh.isRefreshing = isLoading
    }

    override fun showError() {}

    override fun openFriend(uid: String, transitionName: String, args: Bundle) {
        val dataIntent = Intent(context, FriendProfileActivity::class.java)
        dataIntent.putExtra("uid", uid)
        dataIntent.putExtra("transitionName", transitionName)

        startActivity(dataIntent, args)
    }

    override fun openChat(uid: String, name: String, image: String) {
        val chatIntent = Intent(context, ChatMessageActivity::class.java)
        chatIntent.putExtra("uid", uid)
        chatIntent.putExtra("name", name)
        chatIntent.putExtra("photo_url", image)
        startActivity(chatIntent)
    }

    override fun showRegistrationDialog() {
        activity?.supportFragmentManager?.beginTransaction()?.add(ConfirmDialogFragment.newInstance(Consts.ACTION_MODULE),
                ConfirmDialogFragment.TAG)?.commit()
    }

    override fun clearUsers() = adapter.clearData()

    //Updating results of the filtration
    override fun onUpdate() {
        if (::searchPresenter.isInitialized) {
            searchPresenter.onUpdate()
            if (::onUserListScrollListener.isInitialized) onUserListScrollListener.resetState()
        }
    }

    override fun showFullProfileDialog() {
        activity?.supportFragmentManager?.beginTransaction()
                ?.add(ConfirmDialogFragment.newInstance(Consts.FP_MODULE), ConfirmDialogFragment.TAG)
                ?.commit()
    }

    override fun openSlider(sliderList: ArrayList<String>) {
        val intent = Intent(context, SliderActivity::class.java)
        intent.putStringArrayListExtra(Consts.DEFAULT_LIST, sliderList)
        startActivity(intent)
    }

    override fun setHotsLoaded() = infiniteScrollListener!!.setLoaded()

    override fun onConfirm() {
        searchPresenter.openSliderWithDefaults()
    }

    override fun onConfirmPurchase() {
        searchPresenter.confirmHotPurchase()
    }

    override fun showEmpty(show: Boolean) {
        if (show) {
            users_recycler_view.visibility = View.GONE
            users_empty_text.visibility = View.VISIBLE
        } else {
            users_recycler_view.visibility = View.VISIBLE
            users_empty_text.visibility = View.GONE
        }
    }

    override fun onLoadMore() {
        searchPresenter.getHotUsersByPage()
    }

    override fun addHotUsers(hotUsers: MutableList<HotUser>?) {
        hotAdapter.addHots(hotUsers)
    }

    override fun openRewardActivity() {
        val rewardIntent = Intent(activity, RewardVideoActivity::class.java)
        activity?.startActivity(rewardIntent)
    }

    override fun openPurchaseDialog() {
        activity?.supportFragmentManager?.beginTransaction()
                ?.add(PurchaseDialogFragment.newInstance(10), PurchaseDialogFragment.TAG)
                ?.commit()
    }


}
