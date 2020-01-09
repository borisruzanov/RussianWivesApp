package com.borisruzanov.russianwives.mvp.ui.chats

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.OnItemClickListener
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.di.component
import com.borisruzanov.russianwives.models.Contract
import com.borisruzanov.russianwives.models.UserChat
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.chats.adapter.ChatsAdapter
import javax.inject.Inject

class ChatsFragment : MvpAppCompatFragment(), ChatsView {

    // todo: use kotlin syntetic
    lateinit var recyclerChatsList: RecyclerView
    lateinit var chatsAdapter: ChatsAdapter

    lateinit var emptyLayout: RelativeLayout

    private lateinit var mMainView: View

    @Inject
    @InjectPresenter
    lateinit var chatsPresenter: ChatsPresenter

    @ProvidePresenter
    fun provideChatsPresenter() = chatsPresenter

    private val onItemClickCallback = OnItemClickListener.OnItemClickCallback { _, position ->
        Log.d(Contract.TAG, "In onCLICK")
        chatsPresenter.openChat(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_friends, container, false)

        emptyLayout = mMainView.findViewById(R.id.chats_empty_rl)

        recyclerChatsList = mMainView.findViewById(R.id.friends_fragment_recycler_chats)
        recyclerChatsList.layoutManager = LinearLayoutManager(activity)
        recyclerChatsList.setHasFixedSize(true)
        recyclerChatsList.addItemDecoration(DividerItemDecoration(recyclerChatsList.context, DividerItemDecoration.VERTICAL))
        chatsAdapter = ChatsAdapter(onItemClickCallback)
        recyclerChatsList.adapter = chatsAdapter

        chatsPresenter.getUserChatList()

        return mMainView
    }

    /**
     * Getting the list of user's chats and set it to adapter or show empty text
     * @param userChats
     */
    override fun showUserChats(userChats: List<UserChat>) {
        if (!userChats.isEmpty()) {
            Log.d("ChatsDebug", "Chats is not empty")
            recyclerChatsList.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
            recyclerChatsList.post { chatsAdapter.setData(userChats) }
        } else {
            Log.d("ChatsDebug", "Chats is empty")
            recyclerChatsList.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        }
    }

    override fun highlightChats(messageSeen: Boolean) {
//        (activity as MainActivity).highlightChats(messageSeen)
    }

    override fun showErrorMessage() {
        Toast.makeText(activity, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
    }

    //TODO uncomment addUserActivity

    /**
     * Open clicked chat
     * @param uid
     * @param name
     * @param image
     */
    override fun openChat(uid: String, name: String, image: String) {
        val chatIntent = Intent(context, ChatMessageActivity::class.java)
        chatIntent.putExtra("uid", uid)
        chatIntent.putExtra("name", name)
        chatIntent.putExtra("photo_url", image)

        startActivity(chatIntent)
    }

}
