package com.borisruzanov.russianwives.mvp.ui.search


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.R.id.*
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity
import com.borisruzanov.russianwives.mvp.ui.search.adapter.FeedScrollListener
import com.borisruzanov.russianwives.mvp.ui.search.adapter.SearchAdapter
import com.borisruzanov.russianwives.utils.Consts
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_main_tab_search.*
import java.util.Objects

import javax.inject.Inject

class SearchFragment : MvpAppCompatFragment(), SearchView {

    @Inject
    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter() = searchPresenter

    private lateinit var layoutManager: GridLayoutManager
    private var dialogFragment: DialogFragment? = null

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
                Objects.requireNonNull<FragmentActivity>(activity), sharedImageView, ViewCompat.getTransitionName(sharedImageView))

        searchPresenter.openFriend(position, Objects.requireNonNull<Bundle>(options.toBundle()))
    }

    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val adapter = SearchAdapter(onItemClickCallback, onItemChatCallback, onItemLikeCallback)

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var onUserListScrollListener: FeedScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
       // retainInstance = true
        val application = requireActivity().application as App
        application.component.inject(this)
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_main_tab_search, container, false)

        val filterBtn: Button = view.findViewById(R.id.users_filte_btn)
        dialogFragment = FilterDialogFragment()
        usersRecyclerView = view.findViewById(R.id.users_recycler_view)
        emptyTextView = view.findViewById(R.id.users_empty_text)
        swipeRefreshLayout = view.findViewById(R.id.users_swipe_refresh)

        filterBtn.setOnClickListener {
            Log.d("qwe", "show filter dialog")
            dialogFragment?.show(activity?.supportFragmentManager, FilterDialogFragment.TAG)
        }

        layoutManager = GridLayoutManager(activity, 3)

        onUserListScrollListener = object : FeedScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d("UsersListDebug", "in onUserScrollListener and page is $page")
                searchPresenter.setProgressBar(true)
                searchPresenter.getUserList(page)
            }
        }

        if (usersRecyclerView.layoutManager == null) {
            usersRecyclerView.layoutManager = layoutManager
        }

        usersRecyclerView.setHasFixedSize(true)
        usersRecyclerView.adapter = adapter
        usersRecyclerView.addOnScrollListener(onUserListScrollListener)

        swipeRefreshLayout.setOnRefreshListener {
            Log.d("UsersListDebug", "refreshing ...")
            searchPresenter.onUpdate()
            onUserListScrollListener.resetState()
        }

        searchPresenter.setProgressBar(true)
        searchPresenter.getUserList(0)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("UsersListDebug", "in onActivityCreated")
    }

    override fun addUsers(userList: List<FsUser>) {
        searchPresenter.setProgressBar(false)
        //!important! when using FeedScrollListener we need manually tell it about the end of the list
        if (userList.size == 1) {
            onUserListScrollListener.setStopLoading(true)
        }
        else adapter.addUsers(userList)
    }

    override fun setProgressBar(isLoading: Boolean) {
        swipeRefreshLayout.isRefreshing = isLoading
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
        searchPresenter.onUpdate()
    }


    override fun showEmpty(show: Boolean) {
        if (show) {
            usersRecyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        } else {
            usersRecyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        }
    }

}
