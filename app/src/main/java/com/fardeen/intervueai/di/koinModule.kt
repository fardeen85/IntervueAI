package com.fardeen.intervueai.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.fardeen.intervueai.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appmodule = module {

    viewModelOf(::HomeScreenViewModel)

}