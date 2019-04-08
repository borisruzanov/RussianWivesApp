package com.borisruzanov.russianwives.mvp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat.invalidateOptionsMenu
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.R.id.toolbar
import com.borisruzanov.russianwives.R.string.finish
import com.borisruzanov.russianwives.di.component
import com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.FULL_PROFILE_ACH
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
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
import com.borisruzanov.russianwives.mvp.ui.shop.ShopActivity
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity
import com.borisruzanov.russianwives.utils.Consts
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView, FilterDialogFragment.FilterListener,
        ConfirmDialogFragment.ConfirmListener, GenderDialogFragment.GenderListener {

    //MVP
    @Inject
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    //UI
    lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager
    private lateinit var mainPagerAdapter: MainPagerAdapter

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
        setContentView(R.layout.activity_main)

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

        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)

        searchFragment = SearchFragment()
        actionsFragment = ActionsFragment()

        //UI
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mainPresenter.checkForUserExist()
        viewPager.offscreenPageLimit = 3

        tabLayout.setupWithViewPager(viewPager)

        mainPagerAdapter.notifyDataSetChanged()

        Log.d("TabDebug", "Tabs count is ${tabLayout.tabCount}")

        analytics()
        Handler().postDelayed({mainPresenter.showDialogs()}, 3000)

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
            menu.findItem(R.id.sign_out_menu).isVisible = false
            menu.findItem(R.id.menu_my_profile).isVisible = false
            menu.findItem(R.id.login).isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu -> {
                val params = Bundle()
                firebaseAnalytics.logEvent("sign_out", params)
                AuthUI.getInstance().signOut(this).addOnCompleteListener { reload() }
                return true
            }
            R.id.menu_my_profile -> {
                val settingsIntent = Intent(this@MainActivity, MyProfileActivity::class.java)
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
