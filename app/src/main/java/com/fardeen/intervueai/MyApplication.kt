package com.fardeen.intervueai

import android.app.Application
import com.fardeen.intervueai.di.appmodule
import com.fardeen.intervueai.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication :  Application()  {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MyApplication)
            modules(listOf(databaseModule,appmodule))
        }
    }
}