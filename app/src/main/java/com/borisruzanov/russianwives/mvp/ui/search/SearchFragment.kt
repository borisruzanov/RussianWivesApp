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

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity
import com.borisruzanov.russianwives.mvp.ui.main.MainActivity
import com.borisruzanov.russianwives.mvp.ui.search.adapter.FeedScrollListener
import com.borisruzanov.russianwives.mvp.ui.search.adapter.SearchAdapter
import com.borisruzanov.russianwives.mvp.ui.shop.HotAdapter
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity
import com.borisruzanov.russianwives.utils.Consts
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_main_tab_search.*
import java.util.ArrayList
import java.util.Objects

import javax.inject.Inject

class SearchFragment : MvpAppCompatFragment(), SearchView, ConfirmDialogFragment.ConfirmListener, HotAdapter.ItemClickListener {


    @Inject
    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter
    var item: MenuItem? = null
    @ProvidePresenter
    fun providePresenter() = searchPresenter

    private lateinit var layoutManager: GridLayoutManager
    private var dialogFragment = FilterDialogFragment()

    private lateinit var hotAdapter: HotAdapter

    private var hotRecycler: RecyclerView? = null



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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

        // set up the RecyclerView
//        val recyclerView = view?.findViewById<RecyclerView>(R.id.users_hot_recycler)
//        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView?.setLayoutManager(horizontalLayoutManager)
//        hotAdapter = HotAdapter(context, getList())
//        hotAdapter!!.setClickListener(this)
//        recyclerView?.setAdapter(adapter)
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
//        users_hot_recycler.setOnClickListener {
//            Log.d("qwe", "show filter dialog")
//            dialogFragment.show(activity?.supportFragmentManager, FilterDialogFragment.TAG)
//        }

        layoutManager = GridLayoutManager(activity, 3)

//        onUserListScrollListener = object : FeedScrollListener(layoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
//                Log.d("UsersListDebug", "in onUserScrollListener and page is $page")
//                searchPresenter.setProgressBar(true)
//                searchPresenter.getUserList(page)
//            }
//        }
//
//        if (users_recycler_view.layoutManager == null) {
//            users_recycler_view.layoutManager = layoutManager
//        }
//
//        users_recycler_view.setHasFixedSize(true)
//        users_recycler_view.adapter = adapter
//        users_recycler_view.addOnScrollListener(onUserListScrollListener)
//
//        users_swipe_refresh.setOnRefreshListener {
//            Log.d("UsersListDebug", "refreshing ...")
//            onUpdate()
//        }
//
//        searchPresenter.setProgressBar(true)
//        searchPresenter.getUserList(0)
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
            onUserListScrollListener.resetState()
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

    override fun onConfirm() {
        searchPresenter.openSliderWithDefaults()
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

    private fun getList(): List<Int> {
        // data to populate the RecyclerView with
        val viewColors = ArrayList<Int>()
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        viewColors.add(R.drawable.default_avatar)
        return viewColors
    }

}
