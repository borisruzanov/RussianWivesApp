package com.borisruzanov.russianwives

import android.app.Application
import android.content.Context
import android.util.Log
import com.borisruzanov.russianwives.di.component.AppComponent
import com.borisruzanov.russianwives.di.component.DaggerAppComponent
import com.borisruzanov.russianwives.di.module.AppModule

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker


class App : Application() {

    private var mUserDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var sAnalytics: GoogleAnalytics
    lateinit var sTracker: Tracker


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

        if (mAuth!!.currentUser != null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            mUserDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users").child(mAuth!!.currentUser!!.uid)
            //TODO fix-1
            mUserDatabase!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot != null) {
                        mUserDatabase!!.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP)
                        mUserDatabase!!.child("online").setValue(true)
                    } else {
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })


            mUserDatabase = FirebaseDatabase.getInstance().reference
                    .child("OnlineUsers/").child("Female").child(mAuth!!.currentUser!!.uid)
            //TODO fix-1
            mUserDatabase!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot != null) {
                        mUserDatabase!!.child("country").onDisconnect().setValue(ServerValue.TIMESTAMP)
                        mUserDatabase!!.child("gender").setValue(true)
                        mUserDatabase!!.child("image").setValue(true)
                        mUserDatabase!!.child("name").setValue(true)
                        mUserDatabase!!.child("rating").setValue(true)
                        mUserDatabase!!.child("uid").setValue(mAuth!!.currentUser!!.uid)
                    } else {
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

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
     * Gets the default [Tracker] for this [Application].
     * @return tracker
     */
    @Synchronized
    fun getTracker(): Tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.global_tracker)





}
