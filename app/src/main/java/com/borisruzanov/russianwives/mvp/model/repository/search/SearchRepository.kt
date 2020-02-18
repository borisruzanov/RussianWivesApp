package com.borisruzanov.russianwives.mvp.model.repository.search

import android.util.Log
import com.borisruzanov.russianwives.eventbus.StringEvent
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.models.SearchModel
import com.borisruzanov.russianwives.utils.Consts
import com.borisruzanov.russianwives.utils.FirebaseUtils.getUid
import com.borisruzanov.russianwives.utils.FirebaseUtils.isUserExist
import com.borisruzanov.russianwives.utils.UsersListCallback
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import org.greenrobot.eventbus.EventBus
import java.util.*


class SearchRepository {
    companion object {
        const val ITEMS_PER_PAGE = 20
    }

    private var lastUserInPage: DocumentSnapshot? = null
    private val reference: CollectionReference = FirebaseFirestore.getInstance().collection(Consts.USERS_DB)
    private val mDatabase = FirebaseDatabase.getInstance()

    // метод пагинации, который забирает по 20 пользователей
    fun getUsers(filterParams: List<SearchModel>, usersListCallback: UsersListCallback, page: Int) {
        var query: Query = reference

        if (filterParams.isNotEmpty()) {
            for ((key, value) in filterParams) {
                query = query.whereEqualTo(key, value)
            }
        }

        //not forgetting to set to zero when using from another fragment
        if (page == 0)
            lastUserInPage = null


        Log.d("RatingDebug", "Page number is $page and last user uid is ${lastUserInPage?.getString(Consts.UID)}")

        query = query
                .whereEqualTo(Consts.MUST_INFO,Consts.TRUE)
                .whereGreaterThan(Consts.RATING, 0)
                .orderBy(Consts.RATING, Query.Direction.DESCENDING)
                .limit(ITEMS_PER_PAGE.toLong())

        if (lastUserInPage != null) {
            query = query.startAfter(lastUserInPage!!)
        }
        query.get()
                .addOnSuccessListener { documentSnapshots ->
                    Log.d("RatingDebug", "Page number is $page and last user uid is ${lastUserInPage?.getString(Consts.UID)}")
                    putCallbackData(usersListCallback, documentSnapshots)
                }
                .addOnFailureListener { exception ->
                    EventBus.getDefault().post(StringEvent(Consts.FILTER_INDEX))
                    addingIndexInDb(exception.message.toString())
                    Log.w("RatingDebug", "Error getting documents: ", exception)
                }
    }

    private fun addingIndexInDb(message: String) {
        val currentTime = Calendar.getInstance().time
        mDatabase.reference.child("Indexes").child(currentTime.toString()).setValue(message)
    }


    private fun putCallbackData(usersListCallback: UsersListCallback, documentsSnapshot: QuerySnapshot) {
        val fsUserList = ArrayList<FsUser>()
        for (snapshot in documentsSnapshot.documents) {
            if (isUserExist()) {
                //Remove user from the list
                if (snapshot.id != getUid()) {
                    fsUserList.add(snapshot.toObject(FsUser::class.java)!!)
                }
            } else {
                fsUserList.add(snapshot.toObject(FsUser::class.java)!!)
            }
        }

        if (fsUserList.isNotEmpty()) {
            for (user in fsUserList) {
                Log.d("RatingDebug", "User uid is ${user.uid} with ${user.rating} points")
            }
            lastUserInPage = documentsSnapshot.documents.last()
            Log.d("RatingDebug", "Saved lastUserInPage uid is ${lastUserInPage?.get(Consts.UID)} " +
                    "and rating is ${lastUserInPage?.get(Consts.RATING)}")
            //fucking firebase have made me writing this shitty code
            if (fsUserList.size == ITEMS_PER_PAGE) {
                fsUserList.removeAt(fsUserList.size - 1)
            } else if (fsUserList.size == ITEMS_PER_PAGE - 1 && isUserExist()) {
                fsUserList.removeAt(fsUserList.size - 1)
            }
        }
        usersListCallback.setUsers(fsUserList)
    }

}
