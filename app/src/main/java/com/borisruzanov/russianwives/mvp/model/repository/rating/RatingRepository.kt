package com.borisruzanov.russianwives.mvp.model.repository.rating

import android.util.Log

import com.borisruzanov.russianwives.utils.Consts
import com.borisruzanov.russianwives.utils.RatingAchievementsCallback
import com.borisruzanov.russianwives.utils.RatingCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList
import java.util.HashMap

import com.borisruzanov.russianwives.utils.FirebaseUtils.getUid
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class RatingRepository {


    private val usersRef = FirebaseDatabase.getInstance().reference.child(Consts.USERS_DB)
    private val users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB)

    object Achievments {
        val ADDED_PHOTO_RATING = 1
        val ADDED_ETHNICITY_RATING = 1
        val ADDED_RELIGION_RATING = 1
    }

    fun addRating(addPoint: Int) {
        usersRef.child(getUid()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rating: Long

                if (dataSnapshot.child(Consts.RATING).exists()) {
                    rating = dataSnapshot.child(Consts.RATING).getValue(Long::class.java)!!
                    val newRating = rating + addPoint
                    dataSnapshot.child(Consts.RATING).ref.setValue(newRating)
                    //checkForAchive(age, gender, photo)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun checkForAchieve(params: List<String>, achievement: String) {
        var notDefault = true
        users.document(getUid()).get().addOnCompleteListener { task ->
            run {
                val snapshot: DocumentSnapshot? = task.result
                for (value in params) {
                    Log.d("AchDebug", "Value is ${snapshot?.getString(value)} " +
                            "and default is ${snapshot?.getString(value) == Consts.DEFAULT}")
                    if (snapshot?.getString(value) == Consts.DEFAULT) {
                        notDefault = false
                        break
                    }
                }
                if (notDefault) {
                    addAchievement(achievement)
                }
            }
        }
    }

    private fun addAchievement(achievement: String) {
        usersRef.child(getUid()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var achMap: MutableMap<String, Any> = HashMap()

                if (dataSnapshot.child(Consts.ACHIEVEMENTS).exists()) {
                    achMap = dataSnapshot.child(Consts.ACHIEVEMENTS).value as MutableMap<String, Any>
                }
                achMap[achievement] = true
                dataSnapshot.child(Consts.ACHIEVEMENTS).ref.setValue(achMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getRating(uid: String, callback: RatingCallback) {
        usersRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(Consts.RATING).exists()) {
                    val rating = dataSnapshot.child(Consts.RATING).getValue(Long::class.java)!!
                    callback.setRating(rating)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getRating(callback: RatingAchievementsCallback) {
        usersRef.child(getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var rating: Long = 0
                val achievements = ArrayList<String>()
                if (dataSnapshot.child(Consts.RATING).exists()) {
                    rating = dataSnapshot.child(Consts.RATING).getValue(Long::class.java)!!
                }
                if (dataSnapshot.child(Consts.ACHIEVEMENTS).exists()) {
                    val achMap = dataSnapshot.child(Consts.ACHIEVEMENTS).value as HashMap<String, Any>?
                    achievements.addAll(achMap!!.keys)
                }
                callback.setRatingAchievements(rating, achievements)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    companion object {


        val ADDED_PHOTO_ACHIEVE = "true"
        val ADDED_ETHNICITY_ACHIEVE = "true"
        val ADDED_RELIGION_ACHIEVE = "true"
    }

}
