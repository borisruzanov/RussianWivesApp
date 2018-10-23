package com.borisruzanov.russianwives

import android.app.Application
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

class App : Application() {

    private var mUserDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    val component: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()

        component.inject(this)

        mAuth = FirebaseAuth.getInstance()


        if (mAuth!!.currentUser != null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            mUserDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users").child(mAuth!!.currentUser!!.uid)

            Log.d("xx", "user != null")
            mUserDatabase!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    Log.d("xx", "onDataChange")

                    if (dataSnapshot != null) {
                        Log.d("xx", "dataSnapshot != null")
                        mUserDatabase!!.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP)
                        mUserDatabase!!.child("online").setValue(true)
                    } else {
                        Log.d("xx", "dataSnapshot == null")

                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
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


}
