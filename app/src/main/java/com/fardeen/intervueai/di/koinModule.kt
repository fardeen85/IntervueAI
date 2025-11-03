package com.fardeen.intervueai.di


import com.fardeen.intervueai.GeminiRepositoryImpl
import com.fardeen.intervueai.HomeScreenViewModel
import com.fardeen.intervueai.InterviewChatViewModel
import com.fardeen.intervueai.LocalRepositoryImpl
import com.fardeen.intervueai.NetworkStatusViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.fardeen.intervueai.SelectTopicViewModel
import com.fardeen.intervueai.createchatMeta.presentation.createChatMetaViewModel
import com.fardeen.intervueai.local.AppDatabase
import com.fardeen.intervueai.local.ChatDao
import com.fardeen.intervueai.local.LocalDataSource
import com.fardeen.intervueai.remote.RemoteDataSource
import com.fardeen.intevueai.gateway.GeminiRepository
import com.fardeen.intevueai.gateway.LocalRepository
import com.fardeen.intevueai.usecases.CallGeminiUseCase
import com.fardeen.intevueai.usecases.DeleteChatListingUseCase
import com.fardeen.intevueai.usecases.DeleteChatUseCase
import com.fardeen.intevueai.usecases.FetchChatListingDataUseCase
import com.fardeen.intevueai.usecases.FetchLocalDataUseCase
import com.fardeen.intevueai.usecases.SaveChatListingDataUseCase
import com.fardeen.intevueai.usecases.SaveLocalChatUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import kotlin.math.sin


val appmodule = module {


    viewModelOf(::HomeScreenViewModel)
    viewModel{NetworkStatusViewModel(get())}
    single { RemoteDataSource() }
    single { LocalDataSource(get(),get()) }
    single { LocalRepositoryImpl(get()) }.bind(LocalRepository::class)
    single { GeminiRepositoryImpl(get())}.bind(GeminiRepository::class)
    factory { CallGeminiUseCase(get()) }
    factory { SaveChatListingDataUseCase(get()) }
    factory { FetchChatListingDataUseCase(get()) }
    factory { FetchLocalDataUseCase(get()) }
    factory { SaveLocalChatUseCase(get()) }
    factory { DeleteChatListingUseCase(get()) }
    factory { DeleteChatUseCase(get()) }
    viewModel{SelectTopicViewModel(get(),get(),get())}
    viewModel { createChatMetaViewModel(get()) }
    viewModel { InterviewChatViewModel(get(),get(),get(),get(),get(),get()) }



}

