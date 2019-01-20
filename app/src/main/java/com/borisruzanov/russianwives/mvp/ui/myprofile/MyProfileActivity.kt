package com.borisruzanov.russianwives.mvp.ui.myprofile

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.borisruzanov.russianwives.mvp.ui.global.adapter.UserDescriptionListAdapter
import com.borisruzanov.russianwives.OnItemClickListener
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.models.UserDescriptionModel
import com.borisruzanov.russianwives.mvp.ui.profilesettings.ProfileSettingsActivity
import com.borisruzanov.russianwives.mvp.ui.search.SearchFragment
import com.borisruzanov.russianwives.utils.Consts
import com.bumptech.glide.Glide

import butterknife.BindView
import com.borisruzanov.russianwives.di.component
import javax.inject.Inject

class MyProfileActivity : MvpAppCompatActivity(), MyProfileView {

    //todo: use kotlin syntetic
    //UI
    @BindView(R.id.recycler_list_userDescription)
    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var fab: FloatingActionButton
    lateinit var nameText: TextView
    lateinit var ageText: TextView
    lateinit var countryText: TextView
    lateinit var numberOfLikes: TextView
    lateinit var numberOfVisits: TextView
    lateinit var imageView: ImageView

    lateinit var userDescriptionListAdapter: UserDescriptionListAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: MyProfilePresenter

    @Inject
    @ProvidePresenter
    fun provideMyProfilePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        //UI
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = ""

        val collapsingToolbarLayout = findViewById<View>(R.id.collapsing_toolbar_layout) as CollapsingToolbarLayout
        collapsingToolbarLayout.isTitleEnabled = false

        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        toolbar.background.alpha = 125
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recycler_list_userDescription)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        userDescriptionListAdapter = UserDescriptionListAdapter(setOnItemClickCallback())
        recyclerView.adapter = userDescriptionListAdapter

        fab = findViewById(R.id.fab_id)
        fab.setOnClickListener {
            val settingsProfile = Intent(this@MyProfileActivity, ProfileSettingsActivity::class.java)
            startActivity(settingsProfile)
            finish()
        }

        imageView = findViewById(R.id.my_profile_image)
        nameText = findViewById(R.id.my_profile_tv_name)
        ageText = findViewById(R.id.my_profile_tv_age)
        countryText = findViewById(R.id.my_profile_tv_country)
        numberOfLikes = findViewById(R.id.number_of_likes)
        numberOfVisits = findViewById(R.id.number_of_visits)

        supportFragmentManager.beginTransaction().add(R.id.my_profile_list_container, SearchFragment()).commit()

    }

    override fun setUserData(name: String, age: String, country: String, image: String) {
        nameText.text = name
        ageText.text = age
        countryText.text = country
        if (image != Consts.DEFAULT) {
            Glide.with(this)
                    .load(image)
                    .into(imageView)
        }
    }

    override fun setActionsCount(visits: Long, likes: Long) {
        numberOfVisits.text = visits.toString()
        numberOfLikes.text = likes.toString()
    }

    override fun setList(userDescriptionList: List<UserDescriptionModel>) {
        //Setting data to the adapter
        userDescriptionListAdapter.setData(userDescriptionList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setOnItemClickCallback(): OnItemClickListener.OnItemClickCallback {
        return OnItemClickListener.OnItemClickCallback { view, position -> }
    }

    override fun onRestart() {
        super.onRestart()
    }
}
