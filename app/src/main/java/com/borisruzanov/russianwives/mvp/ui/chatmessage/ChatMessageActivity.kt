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
import android.widget.LinearLayout
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.models.Contract
import com.borisruzanov.russianwives.models.Message
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs
import com.borisruzanov.russianwives.mvp.ui.chatmessage.adapter.ChatMessageAdapter
import com.borisruzanov.russianwives.mvp.ui.main.MainScreenActivity
import com.borisruzanov.russianwives.utils.Consts
import com.borisruzanov.russianwives.utils.FirebaseUtils.getUid
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class ChatMessageActivity : MvpAppCompatActivity(), ChatMessageView {

    private val APP_ID = "ca-app-pub-5095813023957397~1146672660"
    private var mAdView: AdView? = null

    //UI
    private lateinit var mChatUser: String
    private lateinit var mLastSeenView: TextView
    private lateinit var mChatMessageView: EditText
    private lateinit var mRefreshLayout: SwipeRefreshLayout
    private lateinit var mActionsBlockLinear: LinearLayout

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var sp: String? = null

    //MVP
    @Inject
    @InjectPresenter
    lateinit var presenter: ChatMessagePresenter

    @ProvidePresenter
    fun provideChatMessagePresenter(): ChatMessagePresenter = presenter

    //Utility
    private val mRootRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Messages")
    private lateinit var mCurrentUserId: String

    //List
    private val messageList = ArrayList<Message>()
    private lateinit var mAdapter: ChatMessageAdapter
    private lateinit var mLinearLayout: LinearLayoutManager
    private var mCurrentPage = 1
    private var itemPos = 0
    private var mLastKey = ""
    private var mPrevKey = ""
    private var mPrefs: Prefs? = null
    private var removedItem = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        val application = application as App
        application.component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mActionsBlockLinear = findViewById(R.id.chat_action_block_linear)

        mChatUser = intent.getStringExtra(Consts.UID)
        mCurrentUserId = getUid()
        mPrefs = Prefs(this)
        increaseUserActivity()
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
        mTitleView.text = intent.getStringExtra(Consts.NAME)
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
        mRefreshLayout.setEnabled(false)

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
            firebaseAnalytics.logEvent("message_sent", null)

            if (mPrefs!!.getValue(Consts.CHAT_LENGTH) != null && !mPrefs!!.getValue(Consts.CHAT_LENGTH).isEmpty()) {
                if (mAdapter.itemCount >= Integer.valueOf(mPrefs!!.getValue(Consts.CHAT_LENGTH))) {
                    if (messageList[0].id != null && messageList.get(0).message != null) {
                        presenter.removeOldMessage(mChatUser, messageList[0].id, messageList.get(0).message)
                        presenter.sendMessage(mChatUser, mChatMessageView.text.toString())
                    }
                    messageList.removeAt(0)
                } else {
                    presenter.sendMessage(mChatUser, mChatMessageView.text.toString())
                }
            }
        }

        adInit()

        phrasePrepare()

    }

    /**
     * Increasing level of the activity of the user
     */
    private fun increaseUserActivity() {
        if (!mPrefs!!.getValue(Consts.USER_ACTIVITY).isEmpty()) {
            if (mPrefs!!.getValue(Consts.USER_ACTIVITY) != Consts.DEFAULT) {
                var prefsValue = Integer.valueOf(mPrefs!!.getValue(Consts.USER_ACTIVITY))
                prefsValue++
                mPrefs!!.setValue(Consts.USER_ACTIVITY, prefsValue.toString())
            } else { //Set user activity to zero if he dont have one
                mPrefs!!.setValue(Consts.USER_ACTIVITY, "0")
            }
        }
    }

    /**
     * If message comes from friendActivity it send immid to friend from user
     */
    private fun phrasePrepare() {
        firebaseAnalytics.logEvent("template_message_get_intent", null)
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val type = bundle.getString(Consts.CHAT_EXTRA_TYPE)
            if (type != null) {
                if (type.equals(Consts.FRIEND_ACTIVITY_PHRASE)) {
                    val message = bundle.getString(Consts.CHAT_EXTRA_MSG)
                    firebaseAnalytics.logEvent("template_message_sent", null)
                    presenter.sendMessage(mChatUser, message)
                } else {
                    firebaseAnalytics.logEvent("template_message_field_focused", null)
                    mChatMessageView.requestFocus()
                }
            }
        }
    }

    private fun adInit() {
        MobileAds.initialize(this, APP_ID)
        mAdView = findViewById<View>(R.id.adView) as AdView?
        val adRequest = AdRequest.Builder().build()
        mAdView?.loadAd(adRequest)
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

        startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.select_image)), GALLERY_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK) {
            Log.d("ImageDebug", "inside if")
            val selectedImageUri = data?.data
            Log.d("ImageDebug", "Uri is " + selectedImageUri!!)
            presenter.sendImage(mChatUser, selectedImageUri)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("ImageDebug", "inside RESULT_CANCELED")
            finish()
        } else {
            Log.d("ImageDebug", "inside else")
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
                } else Log.d("MessageDebug", "MessageKey is null!!!")

                itemPos++
                if (itemPos == 1) {
                    mLastKey = messageKey.toString()
                    mPrevKey = messageKey.toString()
                }
                if (message != null) {
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
        private const val TOTAL_ITEMS_TO_LOAD = 20
        private const val GALLERY_PICK = 1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainScreenActivity::class.java)
        startActivity(intent)
    }
}
