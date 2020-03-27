package com.borisruzanov.russianwives.mvp.ui.search

import android.os.Bundle
import android.util.Log

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.eventbus.ChatEvent
import com.borisruzanov.russianwives.eventbus.ListStringEvent
import com.borisruzanov.russianwives.eventbus.StringEvent
import com.borisruzanov.russianwives.eventbus.UserEvent
import com.borisruzanov.russianwives.models.FsUser
import com.borisruzanov.russianwives.models.HotUser
import com.borisruzanov.russianwives.mvp.model.interactor.coins.CoinsInteractor
import com.borisruzanov.russianwives.mvp.model.interactor.search.SearchInteractor
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.FirebaseUtils.isUserExist
import com.borisruzanov.russianwives.utils.HotUsersCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import com.borisruzanov.russianwives.utils.UsersListCallback
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(private val searchInteractor: SearchInteractor,
                                          private val coinsInteractor: CoinsInteractor) : MvpPresenter<SearchView>() {
    private val fsUsers = ArrayList<FsUser>()
    private val likedUsers = HashSet<String>()
    private val hotUsers = ArrayList<HotUser>()
    private var page = 0

    fun getUserList(page: Int) {
        searchInteractor.getFilteredUserList(usersListCallback, page)
        Log.d("UsersListDebug", "in getUserList and fsUsers size is ${fsUsers.size}")
    }

    fun getHotUsersByPage() {
        // TODO check if page is changing or not
        searchInteractor.getHotUsersByPage(page, callback = HotUsersCallback {
            hotUsers.addAll(it)
            viewState.addHotUsers(it)
            viewState.setHotsLoaded()
        })
        page++
    }

    /**
     * Receiving the list of default values of the user fields
     */
    @Subscribe
    fun getDefaultUserValuesList(event: StringEvent) {
        viewState.userWereNotFoundMsg()
    }

    fun openHotUser(position: Int) {
        Log.d("CarouselDebug", "Uid is ${hotUsers[position - 1].uid}")
        viewState.openHotUser(hotUsers[position - 1].uid)
    }

    fun getHotUsers() {
        searchInteractor.getHotUsers(callback = HotUsersCallback { viewState.addHotUsers(it) })
    }

    fun confirmHotPurchase() = coinsInteractor.purchaseHotOption {}

    fun purchaseHot() {
        coinsInteractor.hasEnoughMoneyForHots { isEnoughMoney ->
            run {
                if (isEnoughMoney) {
                    //open confirm purchase dialog
                    viewState.openPurchaseDialog()
                } else {
                    // open reward activity
                    viewState.openRewardActivity()
                }
            }
        }
    }

    private val usersListCallback = UsersListCallback { userList ->
        if (userList.isNotEmpty()) {
            if (!fsUsers.containsAll(userList)) {
                Log.d("UsersListDebug", "Users list size is ${userList.size}")
                if (fsUsers.isNotEmpty() && fsUsers.last().uid == userList.last().uid) {
                    userList.remove(userList.last())
                }
                Log.d("UsersListDebug", "Users list size after removing is  ${userList.size}")
                fsUsers.addAll(userList)
                Log.d("UsersListDebug", "Add users to fsUsers and fsUsers size is " + fsUsers.size)
            }
            viewState.addUsers(userList)
        } else{
            viewState.userWereNotFoundMsg()
        }
    }

    fun setProgressBar(isLoading: Boolean) {
        viewState.setProgressBar(isLoading)
    }

    fun onUpdate() {
        page = 0
        fsUsers.clear()
        likedUsers.clear()
        viewState.clearUsers()
        getUserList(0)
    }

    fun openSliderWithDefaults() {
        searchInteractor.getDefaultList(callback = StringsCallback { strings ->
            viewState.openSlider(ArrayList<String>(strings))
        })
    }

    fun openSliderWithDefaultsOnlineUsers(){
        searchInteractor.getDefaultList(callback = StringsCallback { strings ->
            EventBus.getDefault().post(ListStringEvent(strings as ArrayList<String>?))
        })
    }

    fun setFriendLiked(position: Int) {
        Log.d("LikeDebug", "Liked Position is $position")
        if (isUserExist()) {
            val friendModel = fsUsers[position]
            if (fsUsers.contains(friendModel)) {
                Log.d("LikeDebug", "Friend name is ${friendModel.name} and fsUsers size is ${fsUsers.size}")
            }
            searchInteractor.isFriendLiked(fsUsers[position].uid, callback = BoolCallback { hasLiked ->
                if (!hasLiked) {
                    searchInteractor.setFriendLiked(fsUsers[position].uid)
                    searchInteractor.addRatingLiked(fsUsers[position].uid)
                    Log.d("LikeDebug", "${fsUsers[position].name} was liked")
                } else Log.d("LikeDebug", "${fsUsers[position].name} WAS NOT liked")
            })
        } else viewState.showRegistrationDialog()
    }

    fun setOnlineFriendLiked(uid: String) {
        searchInteractor.isFriendLiked(uid, callback = BoolCallback { hasLiked ->
            if (!hasLiked) {
                searchInteractor.setFriendLiked(uid)
                searchInteractor.addRatingLiked(uid)
            } else Log.d("LikeDebug", "$uid WAS NOT liked")
        })
    }

    fun openFriend(position: Int, args: Bundle) {
//              val realtimeReference = FirebaseDatabase.getInstance().reference
//        realtimeReference.child("SocialAccounts").child(fsUsers[position].gender).child(fsUsers[position].uid).setValue(fsUsers[position])

        viewState.openFriend(fsUsers[position].uid, fsUsers[position].name, args)
    }

    fun openChat(position: Int) {
        if (isUserExist()) {
            searchInteractor.hasMustInfo(callback = BoolCallback { hasMustInfo ->
                if (hasMustInfo) viewState.openChat(fsUsers[position].uid, fsUsers[position].name, fsUsers[position].image)
                else viewState.showFullProfileDialog()
            })
        } else viewState.showRegistrationDialog()
    }

    fun openChatOnlineUser(uid: String, name: String, image: String) {
        if (isUserExist()) {
            searchInteractor.hasMustInfo(callback = BoolCallback { hasMustInfo ->
                if (hasMustInfo) EventBus.getDefault().post(ChatEvent(name, uid, image))
                else EventBus.getDefault().post(ChatEvent("profile", "", ""))
            })
        } else EventBus.getDefault().post(ChatEvent("registration", "", ""))
    }

    fun registerSubscribers() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

}
