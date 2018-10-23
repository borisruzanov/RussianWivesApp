package com.borisruzanov.russianwives.mvp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.mvp.ui.main.adapter.MainPagerAdapter
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.di.component
import com.borisruzanov.russianwives.mvp.ui.actions.ActionsFragment
import com.borisruzanov.russianwives.mvp.ui.chats.ChatsFragment
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfileActivity
import com.borisruzanov.russianwives.mvp.ui.search.SearchFragment
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

import java.util.Arrays

import javax.inject.Inject

import com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment
import com.borisruzanov.russianwives.mvp.ui.neccessaryinfo.NecessaryInfoDialogFragment
import com.borisruzanov.russianwives.mvp.ui.register.RegisterFragment
import com.borisruzanov.russianwives.utils.Consts

class MainActivity : MvpAppCompatActivity(), MainView, FilterDialogFragment.FilterListener,
        NecessaryInfoDialogFragment.NecessaryInfoListener, ConfirmDialogFragment.ConfirmListener {

    //MVP
    @Inject
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    //UI
    lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager
    private lateinit var mainPagerAdapter: MainPagerAdapter
    private lateinit var unregPagerAdapter: MainPagerAdapter

    //Fragments
    private var dialogFragment: DialogFragment? = null
    private var searchFragment: SearchFragment? = null

    @ProvidePresenter
    internal fun provideMainPresenter(): MainPresenter? {
        return mainPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialogFragment = FilterDialogFragment()
        searchFragment = SearchFragment()

        //UI
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewPager = findViewById(R.id.main_view_pager)
        tabLayout = findViewById(R.id.main_tabs)

        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)
        unregPagerAdapter = MainPagerAdapter(supportFragmentManager)

        mainPresenter.checkForUserExist()
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun setAdapter(isUserExist: Boolean) {
        Log.d("RegDebug", "In setAdapter")
        if (isUserExist) {
            Log.d("RegDebug", "In setAdapter reg")
            mainPagerAdapter.addFragment(searchFragment, getString(R.string.search_title))
            mainPagerAdapter.addFragment(ChatsFragment(), getString(R.string.chats_title))
            mainPagerAdapter.addFragment(ActionsFragment(), getString(R.string.actions_title))
            viewPager.adapter = mainPagerAdapter
        } else {
            Log.d("RegDebug", "In setAdapter unreg")
            unregPagerAdapter.addFragment(searchFragment, getString(R.string.search_title))
            unregPagerAdapter.addFragment(RegisterFragment.newInstance(Consts.CHATS_TAB_NAME), getString(R.string.chats_title))
            unregPagerAdapter.addFragment(RegisterFragment.newInstance(Consts.ACTIONS_TAB_NAME), getString(R.string.actions_title))
            viewPager.adapter = unregPagerAdapter
        }
    }

    override fun showNecessaryInfoDialog(gender: String, age: String) {
        supportFragmentManager.beginTransaction()
                .add(NecessaryInfoDialogFragment.newInstance(gender, age), NecessaryInfoDialogFragment.TAG)
                .commit()
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
        } else {
            menu.findItem(R.id.sign_out_menu).isVisible = false
            menu.findItem(R.id.menu_my_profile).isVisible = false
            menu.findItem(R.id.menu_filter).isVisible = false
            menu.findItem(R.id.login).isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        //if (mainPresenter != null)
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu -> {
                AuthUI.getInstance().signOut(this)
                reload()
                return true
            }
            R.id.menu_my_profile -> {
                val settingsIntent = Intent(this@MainActivity, MyProfileActivity::class.java)
                startActivity(settingsIntent)
                return true
            }
            R.id.menu_filter -> {
                if (viewPager.currentItem == 0) {
                    dialogFragment?.show(supportFragmentManager, FilterDialogFragment.TAG)
                }
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
        Log.d("RegDebug", "In callAuthWindow()")
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            //for checking errors
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.d("RegDebug", "OnActivityResult with ok result")
                mainPresenter.saveUser()
                reload()
            } else {
                //Sign in failed
                if (response == null) {
                }
            }
        }
    }

    override fun onUpdate() {
        searchFragment?.onUpdate()
    }

    override fun setInfo(gender: String, age: String) {
        mainPresenter.setNecessaryInfo(gender, age)
    }

    override fun onConfirm() {
        mainPresenter.openSliderWithDefaults()
    }

    private fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        finish()
        startActivity(intent)
    }

}
