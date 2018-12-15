package com.borisruzanov.russianwives.di.module


import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs
import com.borisruzanov.russianwives.mvp.model.repository.chatmessage.ChatMessageRepository
import com.borisruzanov.russianwives.mvp.model.repository.chats.ChatsRepository
import com.borisruzanov.russianwives.mvp.model.repository.filter.FilterRepository
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {
    @Provides @Singleton fun provideApp() = app

    @Provides fun provideUserRepository() = UserRepository()
    @Provides fun provideRatingRepository() = RatingRepository()
    @Provides fun provideChatMessageRepository() = ChatMessageRepository()
    @Provides fun provideFriendProfileRepository() = FriendRepository()
    @Provides fun provideChatsRepository() = ChatsRepository()

    @Provides fun providePrefsClass() = Prefs(app.applicationContext)
    @Provides fun filterRepository(prefs: Prefs) = FilterRepository(prefs)

    @Provides fun provideSearchRepository() = SearchRepository()
}