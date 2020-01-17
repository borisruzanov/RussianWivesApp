package com.borisruzanov.russianwives

import android.app.Application
import android.content.Context
import com.borisruzanov.russianwives.di.component.AppComponent
import com.borisruzanov.russianwives.di.component.DaggerAppComponent
import com.borisruzanov.russianwives.di.module.AppModule
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs
import com.borisruzanov.russianwives.mvp.model.data.SystemRepository
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso


class App : Application() {

    private var mUserDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var sAnalytics: GoogleAnalytics
    lateinit var sTracker: Tracker
    private lateinit var  mSystemRepository : SystemRepository
    private lateinit var  mPrefs: Prefs


    val component: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    object AnalyticsTracker {
        lateinit var sTracker: Tracker
        private lateinit var sAnalytics: GoogleAnalytics

        fun onCreate(context: Context) {
            sAnalytics = GoogleAnalytics.getInstance(context)
            sTracker = sAnalytics.newTracker(context.getString(R.string.tracker_id))
            sTracker.enableExceptionReporting(true)
            sTracker.enableAdvertisingIdCollection(true)
            sTracker.enableAutoActivityTracking(true)
            sTracker.send(HitBuilders.ScreenViewBuilder().setCustomDimension(1, null).build())
        }
    }


    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        AnalyticsTracker.onCreate(this)
        mAuth = FirebaseAuth.getInstance()
        mPrefs = Prefs(this)
//        mSystemRepository = SystemRepository(mPrefs)
//        mSystemRepository.getConfig()
        if (mAuth!!.currentUser != null) {
            onlineStatusUsersTable()
        }

        //todo: provide picasso by dagger
        val builder = Picasso.Builder(this)
        builder.downloader(OkHttpDownloader(this, Integer.MAX_VALUE.toLong()))
        val built = builder.build()
        built.setIndicatorsEnabled(true)
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)


    }

    /**
     * Setting online / offline status to the user in Users table
     */
    private fun onlineStatusUsersTable() {
        mUserDatabase = FirebaseDatabase.getInstance().reference
                .child("Users").child(mAuth!!.currentUser!!.uid).child("online")
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        mUserDatabase!!.setValue("true")
        mUserDatabase!!.onDisconnect().setValue(ServerValue.TIMESTAMP)
    }


    /**
     * Gets the default [Tracker] for this [Application].
     * @return tracker
     */
    @Synchronized
    fun getTracker(): Tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.global_tracker)

}
