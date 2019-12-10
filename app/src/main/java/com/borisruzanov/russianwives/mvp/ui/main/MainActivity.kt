package com.borisruzanov.russianwives.mvp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.di.component
import com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs
import com.borisruzanov.russianwives.mvp.ui.actions.ActionsFragment
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment
import com.borisruzanov.russianwives.mvp.ui.gender.GenderDialogFragment
import com.borisruzanov.russianwives.mvp.ui.main.adapter.CustomViewPager
import com.borisruzanov.russianwives.mvp.ui.main.adapter.MainPagerAdapter
import com.borisruzanov.russianwives.mvp.ui.mustinfo.MustInfoDialogFragment
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfileActivity
import com.borisruzanov.russianwives.mvp.ui.onlineUsers.OnlineUsersFragment
import com.borisruzanov.russianwives.mvp.ui.rewardvideo.RewardVideoActivity
import com.borisruzanov.russianwives.mvp.ui.search.SearchFragment
import com.borisruzanov.russianwives.mvp.ui.shop.ServicesActivity
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity
import com.borisruzanov.russianwives.utils.Consts
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.analytics.HitBuilders
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView, FilterDialogFragment.FilterListener,
        ConfirmDialogFragment.ConfirmListener, GenderDialogFragment.GenderListener, NavigationView.OnNavigationItemSelectedListener {


    private val APP_ID = "ca-app-pub-5095813023957397~1146672660"
    private var mAdView: AdView? = null

    //MVP
    @Inject
    @InjectPresenter
    lateinit var mPresenter: MainPresenter

    //UI
    lateinit var mToolbar: Toolbar

    //Tabs
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: CustomViewPager
    private lateinit var mViewPagerAdapter: MainPagerAdapter

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mNavigationView: NavigationView
    private lateinit var mDrawerItemSupport: MenuItem
    private lateinit var mDrawerItemSafety: MenuItem
    private lateinit var mDrawerItemLogout: MenuItem
    private lateinit var mDrawerItemLogin: MenuItem

    private lateinit var mCloseDrawerButton: ImageView
    private lateinit var mViewProfileButton: TextView
    private lateinit var mPurchaseSectionButton: LinearLayout
    private lateinit var mFilterButton: RelativeLayout
    private lateinit var mUnregisteredTitle: TextView

    //Fragments
    private var mDialogFragment: DialogFragment? = null
    private var mSearchFragment: SearchFragment? = null
    private var mActionsFragment: ActionsFragment? = null

    private var mUserExist: Boolean = false

    @ProvidePresenter
    internal fun provideMainPresenter() = mPresenter

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer)

        MobileAds.initialize(this, APP_ID)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mDialogFragment = FilterDialogFragment()
        mFilterButton = findViewById(R.id.toolbar_filter_btn)
        mAdView = findViewById<View>(R.id.adView) as AdView?
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener(this)
        mUnregisteredTitle = findViewById(R.id.please_register_to_start_title)

        mUserExist = mPresenter.isUserExist()
        mSearchFragment = SearchFragment()
        mActionsFragment = ActionsFragment()
        mToolbar = this.findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mPresenter.showGenderFirstDialog()
//        sendingToChatActivity()
        analytics()
        drawerInit()
        showDialogs()


        mPresenter.checkForUserExist()

//        loadingListBasedOnUserExist()


        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        mAdView?.loadAd(adRequest)

        val toggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close)
        mDrawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        searchButtonHide(true)

        tabsInit()
    }

    private fun tabsInit() {
        //init views
        mViewPager = findViewById(R.id.main_view_pager)
        mTabLayout = findViewById(R.id.main_tabs)

        //setStatePagerAdapter
        mViewPagerAdapter = MainPagerAdapter(supportFragmentManager)
        if (mUserExist) {
            mViewPagerAdapter.addFragment(OnlineUsersFragment(), getString(R.string.online_users_title))
            mViewPagerAdapter.addFragment(mSearchFragment, getString(R.string.search_title))
            mFilterButton.visibility = VISIBLE
            mUnregisteredTitle.visibility = GONE

        } else {
            mViewPagerAdapter.addFragment(OnlineUsersFragment(), "All Users")
            val tabsTitles = mTabLayout.getChildAt(0) as ViewGroup
            tabsTitles.getChildAt(1).visibility = GONE
            mFilterButton.visibility = GONE
            mUnregisteredTitle.visibility = VISIBLE
            mAdView!!.visibility = GONE
            mTabLayout.visibility = GONE
        }

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL)

        mTabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewPager.currentItem = tab.position
            }
        })
        mViewPager.setAdapter(mViewPagerAdapter)

    }

    fun highlightChats(messageSeen: Boolean) {
//        if (messageSeen) {
//            val viewGroup = mTabLayout.getChildAt(0) as ViewGroup
//            viewGroup.getChildAt(1).setBackgroundColor(ContextCompat.getColor(this, R.color.white))
//        } else {
//            val viewGroup = mTabLayout.getChildAt(0) as ViewGroup
//            viewGroup.getChildAt(1).setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
//        }
    }


    private fun loadingListBasedOnUserExist() {
        if (mPresenter.isUserExist()) {

        } else {
            //Load offline users
        }
    }

    private fun showDialogs() {
        Handler().postDelayed({ mPresenter.showDialogs() }, 3000)
    }


    private fun sendingToChatActivity() {
        if (intent.extras != null) {
            if (intent.extras.get("fromUid") != null) {
                val resultIntent = Intent(this, ChatMessageActivity::class.java)
                resultIntent.putExtra(Consts.UID, intent.extras.get("fromUid").toString())
            }
        }
    }

    private fun drawerInit() {
        val headerView: View = mNavigationView.getHeaderView(0)
        val menu: Menu = mNavigationView.getMenu()
        mDrawerItemSafety = menu.findItem(R.id.drawer_top_menu_safety_item)
        mDrawerItemSafety.isVisible = false
        mNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_top_menu_support_item -> {
                    // handle click
                    true
                }
                R.id.drawer_top_menu_safety_item -> {
                    // handle click
                    true
                }
                R.id.drawer_top_menu_logout_item -> {
                    true
                }
                R.id.drawer_top_menu_login_item -> {
                    true
                }
                else -> false
            }
        }
        mCloseDrawerButton = headerView.findViewById(R.id.drawer_header_close_btn)
        mCloseDrawerButton.setOnClickListener {
            onBackPressed()
        }
        mViewProfileButton = headerView.findViewById(R.id.drawer_header_view_profile)
        mViewProfileButton.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, MyProfileActivity::class.java)
            startActivity(settingsIntent)
        }
        mPurchaseSectionButton = headerView.findViewById(R.id.drawer_header_purchase_section)
        mPurchaseSectionButton.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, ServicesActivity::class.java)
            startActivity(settingsIntent)
        }
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.drawer_top_menu_logout_item -> AuthUI.getInstance().signOut(this).addOnCompleteListener { reload() }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun analytics() {
        val tracker = App.AnalyticsTracker.sTracker

        /* val params = Bundle()
         params.putString("param_One", "First param")
         params.putString("param_Two", "Second param")
         firebaseAnalytics.logEvent("some_event_name", params)*/

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "some_id")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "some_name")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "content_type")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

    }

    override fun setAdapter(isUserExist: Boolean) {
        mViewPager.adapter = mViewPagerAdapter
    }

    override fun showGenderDialog() {
        supportFragmentManager.beginTransaction().add(GenderDialogFragment(), GenderDialogFragment.TAG).commit()
    }

    override fun showMustInfoDialog() {
        supportFragmentManager.beginTransaction().add(MustInfoDialogFragment(), MustInfoDialogFragment.TAG).commit()
    }

    override fun showAdditionalInfoDialog() {
        supportFragmentManager.beginTransaction()
                .add(ConfirmDialogFragment.newInstance(Consts.SLIDER_MODULE), ConfirmDialogFragment.TAG)
                .commit()
    }

    override fun openSlider(stringList: ArrayList<String>) {
        val intent = Intent(this, SliderActivity::class.java)
        intent.putStringArrayListExtra(Consts.DEFAULT_LIST, stringList)
        startActivity(intent)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        invalidateOptionsMenu()
        validateMenuOnAuth(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun validateMenuOnAuth(menu: Menu) {
        if (mPresenter.isUserExist()) {
            menu.findItem(R.id.login).isVisible = false
            //mPresenter.showDialogs()
        } else {
            mToolbar.setNavigationIcon(null);
            menu.findItem(R.id.shop).isVisible = false
            menu.findItem(R.id.chats).isVisible = false
            menu.findItem(R.id.login).isVisible = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun searchButtonHide(result: Boolean) {
        if (!result) {
            Log.d("ss", "!result")
//            mFilterButton.visibility = View.GONE
        } else {
            Log.d("ss", "else result")
//            mFilterButton.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shop -> {
                val settingsIntent = Intent(this@MainActivity, RewardVideoActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            R.id.login -> {
                callAuthWindow()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun callAuthWindow() {

        val tracker = App.AnalyticsTracker.sTracker
        tracker.setScreenName("AUTH Activity")
        tracker.send(HitBuilders.ScreenViewBuilder().build())
        firebaseAnalytics.logEvent("registration_starts", null)

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList<AuthUI.IdpConfig>(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.FacebookBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN)
    }

    fun callFilter() {
//        mDialogFragment.show(activity?.supportFragmentManager, FilterDialogFragment.TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    firebaseAnalytics.logEvent("registration_completed", null)
                    mPresenter.saveUser()
                    reload()
                } else {
                    val bundle = Bundle()
                    bundle.putString("registration_error_type", response?.error?.errorCode.toString())
                    firebaseAnalytics.logEvent("registration_failed", bundle)
                    if (response == null) {
                    }
                }
            }
        }
    }

    override fun onUpdate() {
        mSearchFragment?.onUpdate()
    }

    override fun setGender(gender: String) {
        mPresenter.setGender(gender)
        onUpdate()
    }

    override fun onConfirm() {
        mPresenter.showMustInfoDialog()
    }

    private fun reload() {
        mPresenter.makeDialogOpenDateDefault()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        finish()
        startActivity(intent)
    }

}
