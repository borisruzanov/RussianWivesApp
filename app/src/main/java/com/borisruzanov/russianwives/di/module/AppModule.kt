package com.borisruzanov.russianwives.di.module


import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.mvp.model.data.SystemRepository
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs
import com.borisruzanov.russianwives.mvp.model.repository.hots.HotUsersRepository
import com.borisruzanov.russianwives.mvp.model.repository.chatmessage.ChatMessageRepository
import com.borisruzanov.russianwives.mvp.model.repository.chats.ChatsRepository
import com.borisruzanov.russianwives.mvp.model.repository.coins.CoinsRepository
import com.borisruzanov.russianwives.mvp.model.repository.filter.FilterRepository
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {
    @Provides @Singleton fun provideApp() = app

    @Provides fun provideRatingRepository() = RatingRepository()
    @Provides fun provideChatMessageRepository() = ChatMessageRepository()
    @Provides fun provideFriendProfileRepository(prefs: Prefs) = FriendRepository(prefs)
    @Provides fun provideChatsRepository() = ChatsRepository()

    @Provides fun providePrefsClass() = Prefs(app.applicationContext)
    @Provides fun provideUserRepository(prefs: Prefs) = UserRepository(prefs)
    @Provides fun filterRepository(prefs: Prefs) = FilterRepository(prefs)

    @Provides fun provideSearchRepository() = SearchRepository()
    @Provides fun provideSliderRepository() = SliderRepository()
    @Provides fun provideCoinsRepository() = CoinsRepository()
    @Provides fun provideHotUsersRepository(prefs: Prefs) = HotUsersRepository(prefs)
    @Provides fun provideSystemRepository(prefs: Prefs) = SystemRepository(prefs)

}