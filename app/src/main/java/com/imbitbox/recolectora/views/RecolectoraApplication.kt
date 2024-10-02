package com.imbitbox.recolectora.views

import android.app.Application
import com.imbitbox.recolectora.config.defaultModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


@HiltAndroidApp
class RecolectoraApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@RecolectoraApplication)
            modules(defaultModule)
        }
    }
}