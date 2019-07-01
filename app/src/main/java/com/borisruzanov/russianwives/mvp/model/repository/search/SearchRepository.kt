package com.borisruzanov.russianwives.mvp.model.repository.search

import android.util.Log
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.models.SearchModel
import com.borisruzanov.russianwives.utils.Consts
import com.borisruzanov.russianwives.utils.UsersListCallback

import java.util.ArrayList

import com.borisruzanov.russianwives.utils.FirebaseUtils.getUid
import com.borisruzanov.russianwives.utils.FirebaseUtils.isUserExist
import com.google.firebase.firestore.*


class SearchRepository {
    companion object {
        const val ITEMS_PER_PAGE = 20
    }

    private var lastUserInPage: DocumentSnapshot? = null
    private val reference: CollectionReference = FirebaseFirestore.getInstance().collection(Consts.USERS_DB)

    // метод пагинации, который забирает по 20 пользователей
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
            lastUserInPage = null


        Log.d("RatingDebug", "Page number is $page and last user uid is ${lastUserInPage?.getString(Consts.UID)}")

        // Log.d("RatingDebug", "Last user in page is $lastUserInPage")

        //ВОТ ТУТ У НАС ПО ИДЕЕ НУЛЕВОЙ ОБЪЕКТ И ПОДГРУЖАЕТСЯ ПЕРВАЯ СТРАНИЦА
        // КОГДА НУЛЕВОЙ СТАЛ ДВАДЦАТЫМ ТО ПРИ ЗАПРОСЕ ОН ПЕРЕДАЕТСЯ В ПАРАМЕТР И QUERY ВОЗВРАЩАЕТСЯ 21+ ОБЪЕКТЫ И ОБНОВЛЯЕТ НУЛЕВОЙ НА
        // Схема работы:
        query = query
                .whereGreaterThan(Consts.RATING, 0)
                .orderBy(Consts.RATING, Query.Direction.DESCENDING)
                .limit(ITEMS_PER_PAGE.toLong())

        if (lastUserInPage != null) {
            query = query.startAfter(lastUserInPage!!)
        }
                query.get()
                .addOnSuccessListener { documentSnapshots ->
                    putCallbackData(usersListCallback, documentSnapshots)
                }
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

        //todo: show "not find" photo by this filter params
        if (fsUserList.isNotEmpty()) {
            for (user in fsUserList){
                Log.d("RatingDebug", "User uid is ${user.uid} with ${user.rating} points")
            }
            //Вот видишь ты его тут сетаешь а надо его обратно перекидывать в точку запроса
            // Поэтому при запросе он у тебя не тот, который ты засетал, перекидывай его обратно я думаю  как то так, точно не знаю
            //У тебя же эта переменная не глобальная, как ты ее можешь тут сохра
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
