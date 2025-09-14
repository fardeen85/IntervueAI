package com.fardeen.intervueai.di


import com.fardeen.intervueai.GeminiRepositoryImpl
import com.fardeen.intervueai.HomeScreenViewModel
import com.fardeen.intervueai.NetworkStatusViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.fardeen.intervueai.SelectTopicViewModel
import com.fardeen.intervueai.local.AppDatabase
import com.fardeen.intervueai.local.ChatDao
import com.fardeen.intervueai.remote.RemoteDataSource
import com.fardeen.intevueai.gateway.GeminiRepository
import com.fardeen.intevueai.usecases.CallGeminiUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind


val appmodule = module {


    viewModelOf(::HomeScreenViewModel)
    viewModel{NetworkStatusViewModel(get())}
    single { RemoteDataSource() }
    single { GeminiRepositoryImpl(get())}.bind(GeminiRepository::class)
    factory { CallGeminiUseCase(get()) }
    viewModel{SelectTopicViewModel(get())}



}

