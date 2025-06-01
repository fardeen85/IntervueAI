package com.fardeen.intervueai

import android.app.Application
import com.fardeen.intervueai.di.appmodule
import org.koin.core.context.startKoin

class MyApplication :  Application()  {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(appmodule)
        }
    }
}