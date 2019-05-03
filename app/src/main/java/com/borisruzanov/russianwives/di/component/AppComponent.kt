package com.borisruzanov.russianwives.di.component

import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.di.module.AppModule
import com.borisruzanov.russianwives.mvp.ui.actions.ActionsPresenter
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessagePresenter
import com.borisruzanov.russianwives.mvp.ui.actions.ActionsFragment
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity
import com.borisruzanov.russianwives.mvp.ui.chats.ChatsFragment
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment
import com.borisruzanov.russianwives.mvp.ui.friendprofile.FriendProfileActivity
import com.borisruzanov.russianwives.mvp.ui.main.MainPresenter
import com.borisruzanov.russianwives.mvp.ui.main.MainActivity
import com.borisruzanov.russianwives.mvp.ui.mustinfo.MustInfoDialogFragment
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfileActivity
import com.borisruzanov.russianwives.mvp.ui.rewardvideo.RewardVideoActivity
import com.borisruzanov.russianwives.mvp.ui.search.SearchFragment
import dagger.Component
import javax.inject.Singleton


@Singleton @Component(modules = [AppModule::class])
interface AppComponent {
    // todo: separate to modules
    fun inject(app: App)

    fun inject(mainActivity: MainActivity)
//    fun inject(mainPresenter: MainPresenter)

    fun inject(actionsFragment: ActionsFragment)
//    fun inject(actionsPresenter: ActionsPresenter)

    fun inject(chatMessageActivity: ChatMessageActivity)
//    fun inject(chatMessagePresenter: ChatMessagePresenter)

    fun inject(friendProfileActivity: FriendProfileActivity)
    fun inject(chatsFragment: ChatsFragment)
    fun inject(filterDialogFragment: FilterDialogFragment)
    fun inject(myProfileActivity: MyProfileActivity)
    fun inject(searchFragment: SearchFragment)
    fun inject(mustInfoDialogFragment: MustInfoDialogFragment)
    fun inject(rewardVideoActivity: RewardVideoActivity)
}