package com.fardeen.intervueai.di

import androidx.room.Room
import com.fardeen.intervueai.LocalRepositoryImpl
import com.fardeen.intervueai.local.AppDatabase
import com.fardeen.intervueai.local.ChatDao
import com.fardeen.intervueai.local.ChatListingDao
import com.fardeen.intevueai.gateway.LocalRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {

    // Provide AppDatabase singleton
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // Provide UserDao
    single<ChatDao> {
        get<AppDatabase>().chatDao()
    }

    single<ChatListingDao>{

        get<AppDatabase>().chatListingDao()
    }

    single { LocalRepositoryImpl(get()) }.bind(LocalRepository::class)
}