package com.borisruzanov.russianwives.mvp.model.repository.search

import android.util.Log
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.models.SearchModel
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository
import com.borisruzanov.russianwives.utils.Consts
import com.borisruzanov.russianwives.utils.UsersListCallback
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

import java.util.ArrayList

import com.borisruzanov.russianwives.utils.FirebaseUtils.getUid
import com.borisruzanov.russianwives.utils.FirebaseUtils.isUserExist



class SearchRepository {
    companion object {
        const val ITEMS_PER_PAGE = 20
    }

    private var lastUserInPage = ""
    private val reference: CollectionReference = FirebaseFirestore.getInstance().collection(Consts.USERS_DB)

    //todo: rename getUsersNextPage(isFirstPage: Boolean)
    fun getUsers(filterParams: List<SearchModel>, usersListCallback: UsersListCallback, page: Int) {
        var query: Query = reference

        if (filterParams.isNotEmpty()) {
            for ((key, value) in filterParams) {
                Log.d("FilterDebug", "Key is $key and value is $value")
                query = query.whereEqualTo(key, value)
            }
        }

        //not forgetting to set to zero when using from another fragment
        if (page == 0)
            lastUserInPage = ""

        query
                //todo: .orderBy('rank')
                //todo: add where user did not liked this one
                .orderBy(Consts.NAME)
                .startAt(lastUserInPage) //todo: BUG IN HERE - we need to show only first n-1 users
                .limit(ITEMS_PER_PAGE.toLong())
                .get()
                //todo: addOnSuccessListener instead
                .addOnCompleteListener { task ->
                    putCallbackData(usersListCallback, task) }
    }


    private fun putCallbackData(usersListCallback: UsersListCallback, task: Task<QuerySnapshot>) {
        val fsUserList = ArrayList<FsUser>()
        for (snapshot in task.result!!.documents) {
            if (isUserExist()) {
                //Remove user from the list
                if (snapshot.id != getUid()) {
                    fsUserList.add(snapshot.toObject(FsUser::class.java)!!)
                }
            } else {
                fsUserList.add(snapshot.toObject(FsUser::class.java)!!)
            }
        }

        //todo: show "not find" photo by this filter params
        if (fsUserList.isNotEmpty()) {
            lastUserInPage = fsUserList[fsUserList.size - 1].name //todo: use .uid but it's not working this way

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
