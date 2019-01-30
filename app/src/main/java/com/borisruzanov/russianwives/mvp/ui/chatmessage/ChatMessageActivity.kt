package com.borisruzanov.russianwives.mvp.ui.chatmessage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.mvp.ui.chatmessage.adapter.ChatMessageAdapter
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.models.Contract
import com.borisruzanov.russianwives.models.Message
import com.bumptech.glide.Glide
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import javax.inject.Inject

import de.hdodenhof.circleimageview.CircleImageView

import com.borisruzanov.russianwives.models.Contract.RC_PHOTO_PICKER
import com.borisruzanov.russianwives.utils.Consts
import com.borisruzanov.russianwives.utils.FirebaseUtils.getUid
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.HashMap

class ChatMessageActivity : MvpAppCompatActivity(), ChatMessageView {

    //UI
    private lateinit var mChatUser: String
    private lateinit var mLastSeenView: TextView
    private lateinit var mChatMessageView: EditText
    private lateinit var mRefreshLayout: SwipeRefreshLayout

    private var sp: String? = null

    //MVP
    @Inject
    @InjectPresenter
    lateinit var presenter: ChatMessagePresenter

    @ProvidePresenter
    fun provideChatMessagePresenter(): ChatMessagePresenter = presenter

    //Utility
    private val mRootRef: DatabaseReference =  FirebaseDatabase.getInstance().reference.child("Messages")
    private lateinit var mCurrentUserId: String

    //List
    private val messageList = ArrayList<Message>()
    private lateinit var mAdapter: ChatMessageAdapter
    private lateinit var mLinearLayout: LinearLayoutManager
    private var mCurrentPage = 1
    private var itemPos = 0
    private var mLastKey = ""
    private var mPrevKey = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        val application = application as App
        application.component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mChatUser = intent.getStringExtra("uid")
        mCurrentUserId = getUid()

        Log.d("MessagesDebug", """Uid of mChatUser is ${intent.getStringExtra("uid")}""")
        Log.d("MessagesDebug", """Uid of mCurrentUserId is ${getUid()}""")

        //UI
        val mChatToolbar = findViewById<View>(R.id.chat_app_bar) as Toolbar
        setSupportActionBar(mChatToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val actionBar = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarView = inflater.inflate(R.layout.chat_custom_bar, null)
        actionBar?.customView = actionBarView
        //Custom Action bar Items
        val mTitleView = findViewById<TextView>(R.id.custom_bar_title)
        mTitleView.text = intent.getStringExtra("name")
        val mProfileImage = findViewById<CircleImageView>(R.id.custom_bar_image)
        Glide.with(this).load(intent.getStringExtra("photo_url")).into(mProfileImage)


        //List of Message
        mLinearLayout = LinearLayoutManager(this)
        mAdapter = ChatMessageAdapter(intent.getStringExtra("photo_url"), intent.getStringExtra("name"))

        messages_list.setHasFixedSize(true)
        messages_list.layoutManager = mLinearLayout
        messages_list.adapter = mAdapter
        //TODO Rename Item layout from message_single_layout
        //TODO Rename all variables

        //Buttons
        val mChatAddBtn = findViewById<View>(R.id.chat_add_btn) as ImageButton
        mChatAddBtn.setOnClickListener { addFile() }
        val mChatSendBtn = findViewById<View>(R.id.chat_send_btn) as ImageButton
        mChatMessageView = findViewById<View>(R.id.chat_message_view) as EditText

        //Pagination
        mRefreshLayout = findViewById<View>(R.id.message_swipe_layout) as SwipeRefreshLayout

        //Utility
        mLastSeenView = findViewById<View>(R.id.custom_bar_seen) as TextView

        //Loading messages first time
        loadMessages()
        for (message in messageList) {
            Log.d("ccc", "Message - " + message.message + "from - " + message.from +
                    "timestamp - " + message.time)
        }

        mRefreshLayout.setOnRefreshListener {
            mCurrentPage++
            itemPos = 0
            loadMoreMessages()
        }

        mChatSendBtn.setOnClickListener {
            //presenter.initChat(mChatUser)
            presenter.sendMessage(mChatUser, mChatMessageView.text.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                onBackPressed()
                //handle the home button onClick event here.
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setEmptyMessage() {
        mChatMessageView.setText("")
    }

    private fun addFile() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        Log.d(Contract.CHAT_LIST, "inside addFile")

        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            Log.d(Contract.CHAT_LIST, "inside if")
            val selectedImageUri = data.data
            Log.d(Contract.CHAT_LIST, "Uri is " + selectedImageUri!!)
            presenter.sendImage(mChatUser, selectedImageUri)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d(Contract.CHAT_LIST, "inside RESULT_CANCELED")
            finish()
        } else {
            Log.d(Contract.CHAT_LIST, "inside bbbbbbbdfdfgdfgdfgdfg")
        }
    }

    /**
     * Loading messages after swiping 2+ times
     */
    private fun loadMoreMessages() {
        val messageRef = mRootRef.child(mCurrentUserId).child(mChatUser)
        val messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10)
        messageQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val message = dataSnapshot.getValue(Message::class.java)
                val messageKey = dataSnapshot.key

                if (mPrevKey != messageKey) {
                    messageList.add(itemPos++, message!!)
                } else {
                    mPrevKey = mLastKey
                }
                if (itemPos == 1) {
                    mLastKey = messageKey.toString()
                }
                mAdapter.notifyDataSetChanged()
                mRefreshLayout.isRefreshing = false
                mLinearLayout.scrollToPosition(itemPos)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    /**
     * Load messages only once
     */
    private fun loadMessages() {
        val messageRef = mRootRef.child(mCurrentUserId).child(mChatUser)
        val messageQuery = messageRef.limitToLast(TOTAL_ITEMS_TO_LOAD)
        messageQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val message = dataSnapshot.getValue(Message::class.java)
                val messageKey = dataSnapshot.key

                //Set parameter seen true for all messages
                if (messageKey != null) {
                    Log.d("MessageDebug", "MessageKey is $messageKey")
                    val messageMap = HashMap<String, Any>()
                    messageMap["seen"] = true

                    mRootRef.child(mCurrentUserId).child(mChatUser).child(messageKey).updateChildren(messageMap)
                }
                else Log.d("MessageDebug", "MessageKey is null!!!")

                itemPos++
                if (itemPos == 1) {
                    mLastKey = messageKey.toString()
                    mPrevKey = messageKey.toString()
                }
                if(message != null) {
                    messageList.add(message)
                }
                mAdapter.setData(messageList)
                messages_list.scrollToPosition(messageList.size - 1)
                mRefreshLayout.isRefreshing = false
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MessagesDebug", "Error is " + databaseError.message)
            }
        })
    }

    companion object {
        //Pagination
        private const val TOTAL_ITEMS_TO_LOAD = 10
        private const val GALLERY_PICK = 1
    }

}
