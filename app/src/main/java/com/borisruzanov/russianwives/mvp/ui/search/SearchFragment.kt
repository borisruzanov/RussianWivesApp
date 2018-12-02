package com.borisruzanov.russianwives.mvp.ui.search


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity
import com.borisruzanov.russianwives.mvp.ui.search.adapter.FeedScrollListener
import com.borisruzanov.russianwives.mvp.ui.search.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_main_tab_search.*
import java.util.Objects

import javax.inject.Inject

class SearchFragment : MvpAppCompatFragment(), SearchView {

    @Inject
    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter() = searchPresenter

    private lateinit var layoutManager : GridLayoutManager

    private val onItemChatCallback = { view: View, position: Int -> searchPresenter.openChat(position) }
    private val onItemLikeCallback = { view: View, position: Int -> searchPresenter.setFriendLiked(position) }

    private val onItemClickCallback = { view: View, position: Int ->
        val sharedImageView = view as ImageView
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                Objects.requireNonNull<FragmentActivity>(activity), sharedImageView, ViewCompat.getTransitionName(sharedImageView))

        searchPresenter.openFriend(position, Objects.requireNonNull<Bundle>(options.toBundle()))
    }

    private val adapter = SearchAdapter(onItemClickCallback, onItemChatCallback, onItemLikeCallback)

    private lateinit var onUserListScrollListener : FeedScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        val application = requireActivity().application as App
        application.component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_tab_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layoutManager = GridLayoutManager(activity, 3)

        onUserListScrollListener =  object : FeedScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
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
            searchPresenter.onUpdate()
            onUserListScrollListener.resetState()
        }

        searchPresenter.setProgressBar(true)
        searchPresenter.getUserList(0)
    }

    override fun addUsers(userList: List<FsUser>) {
        searchPresenter.setProgressBar(false)
        //!important! when using FeedScrollListener we need manually tell it about the end of the list
        if (userList.size == 1) {
            onUserListScrollListener.setStopLoading(true)
        } else {
            adapter.addUsers(userList)
        }
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


    //Updating results of the filtration
    override fun onUpdate() {
        adapter.clearData()
        searchPresenter.getUserList(0)
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

    override fun onDestroy() {
        //if(users_recycler_view.layoutManager != null) users_recycler_view.removeOnScrollListener(onUserListScrollListener)
        super.onDestroy()
    }
}
