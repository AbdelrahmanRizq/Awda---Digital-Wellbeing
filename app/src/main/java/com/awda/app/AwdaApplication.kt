package com.awda.app

import android.app.Application
import com.awda.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Abdelrahman Rizq
 */

class AwdaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidLogger()
            androidContext(this@AwdaApplication)
            modules(appModule)
        }
    }
}