package com.borisruzanov.russianwives.mvp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.di.component
import com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN
import com.borisruzanov.russianwives.mvp.ui.actions.ActionsFragment
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.chats.ChatsFragment
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment
import com.borisruzanov.russianwives.mvp.ui.gender.GenderDialogFragment
import com.borisruzanov.russianwives.mvp.ui.main.adapter.MainPagerAdapter
import com.borisruzanov.russianwives.mvp.ui.mustinfo.MustInfoDialogFragment
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfileActivity
import com.borisruzanov.russianwives.mvp.ui.register.RegisterFragment
import com.borisruzanov.russianwives.mvp.ui.search.SearchFragment
import com.borisruzanov.russianwives.mvp.ui.rewardvideo.RewardVideoActivity
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

class MainActivity : MvpAppCompatActivity(), MainView, FilterDialogFragment.FilterListener,
        ConfirmDialogFragment.ConfirmListener, GenderDialogFragment.GenderListener, NavigationView.OnNavigationItemSelectedListener {


    private val APP_ID = "ca-app-pub-5095813023957397~1146672660"
    private var mAdView: AdView? = null

    //MVP
    @Inject
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    //UI
    lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager
    private lateinit var mainPagerAdapter: MainPagerAdapter

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var closeDrawer: ImageView
    private lateinit var viewProfile: TextView
    private lateinit var purchaseSection: LinearLayout
    private lateinit var filterBtn: RelativeLayout

    //Fragments
    private var dialogFragment: DialogFragment? = null
    private var searchFragment: SearchFragment? = null
    private var actionsFragment: ActionsFragment? = null

    @ProvidePresenter
    internal fun provideMainPresenter() = mainPresenter

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer)

        MobileAds.initialize(this, APP_ID)


        if (intent.extras != null) {
            if (intent.extras.get("fromUid") != null) {
                val resultIntent = Intent(this, ChatMessageActivity::class.java)
                resultIntent.putExtra(Consts.UID, intent.extras.get("fromUid").toString())
            }
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        dialogFragment = FilterDialogFragment()
        viewPager = findViewById(R.id.main_view_pager)
        tabLayout = findViewById(R.id.main_tabs)
        filterBtn = findViewById(R.id.toolbar_filter_btn)

        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)

        searchFragment = SearchFragment()
        actionsFragment = ActionsFragment()

        //UI

        toolbar = this.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        mainPresenter.checkForUserExist()
        viewPager.offscreenPageLimit = 3

        tabLayout.setupWithViewPager(viewPager)

        mainPagerAdapter.notifyDataSetChanged()

        Log.d("TabDebug", "Tabs count is ${tabLayout.tabCount}")

        analytics()
        Handler().postDelayed({ mainPresenter.showDialogs() }, 3000)

        mAdView = findViewById<View>(R.id.adView) as AdView?
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        mAdView?.loadAd(adRequest)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        val headerView: View = navigationView.getHeaderView(0)

        closeDrawer = headerView.findViewById(R.id.drawer_header_close_btn)
        closeDrawer.setOnClickListener {
            onBackPressed()
        }
        viewProfile = headerView.findViewById(R.id.drawer_header_view_profile)
        viewProfile.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, MyProfileActivity::class.java)
            startActivity(settingsIntent)
        }
        purchaseSection = headerView.findViewById(R.id.drawer_header_purchase_section)
        purchaseSection.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, ServicesActivity::class.java)
            startActivity(settingsIntent)
        }

        searchButtonHide(true)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.getItemId()) {
            R.id.drawer_top_menu_third_item ->   AuthUI.getInstance().signOut(this).addOnCompleteListener { reload() }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
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
        mainPagerAdapter.addFragment(searchFragment, getString(R.string.search_title))
        if (isUserExist) {
            toolbar.subtitle = ""
            mainPagerAdapter.addFragment(ChatsFragment(), getString(R.string.chats_title))
            mainPagerAdapter.addFragment(actionsFragment, getString(R.string.actions_title))
        } else {
            toolbar.subtitle = getString(R.string.please_register_to_start)
            mainPagerAdapter.addFragment(RegisterFragment.newInstance(Consts.CHATS_TAB_NAME), getString(R.string.chats_title))
            mainPagerAdapter.addFragment(RegisterFragment.newInstance(Consts.ACTIONS_TAB_NAME), getString(R.string.actions_title))
        }
        viewPager.adapter = mainPagerAdapter
    }

    override fun showGenderDialog() {
        supportFragmentManager.beginTransaction().add(GenderDialogFragment(), GenderDialogFragment.TAG).commit()
    }

    override fun showMustInfoDialog() {
        supportFragmentManager.beginTransaction().add(MustInfoDialogFragment(), MustInfoDialogFragment.TAG).commit();
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
        if (mainPresenter.isUserExist()) {
            menu.findItem(R.id.login).isVisible = false
            //mainPresenter.showDialogs()
        } else {
            menu.findItem(R.id.shop).isVisible = false
            menu.findItem(R.id.chats).isVisible = false
            menu.findItem(R.id.login).isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun searchButtonHide(result: Boolean) {
        if (!result) {
            Log.d("ss", "!result")
//            filterBtn.visibility = View.GONE
        } else {
            Log.d("ss", "else result")
//            filterBtn.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shop -> {
                val settingsIntent = Intent(this@MainActivity, RewardVideoActivity::class.java)
                startActivity(settingsIntent)
                return true
            }
            R.id.login -> {
                callAuthWindow()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
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
//        dialogFragment.show(activity?.supportFragmentManager, FilterDialogFragment.TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            //for checking errors
            if (data != null) {
                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    firebaseAnalytics.logEvent("registration_completed", null)
                    mainPresenter.saveUser()
                    reload()
                } else {
                    //Sign in failed
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
        searchFragment?.onUpdate()
    }

    override fun setGender(gender: String) {
        mainPresenter.setGender(gender)
        onUpdate()
    }

    override fun onConfirm() {
        mainPresenter.showMustInfoDialog()
    }

    fun highlightChats(messageSeen: Boolean) {
        if (messageSeen) {
            val viewGroup = tabLayout.getChildAt(0) as ViewGroup
            viewGroup.getChildAt(1).setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        } else {
            val viewGroup = tabLayout.getChildAt(0) as ViewGroup
            viewGroup.getChildAt(1).setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        }
    }

    private fun reload() {
        mainPresenter.makeDialogOpenDateDefault()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        finish()
        startActivity(intent)
    }

}
