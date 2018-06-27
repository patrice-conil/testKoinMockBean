package com.pconil.testKoin

import android.app.Application
import com.pconil.testKoin.BuildConfig
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.log.EmptyLogger


open class MyApplication: Application() {

    override fun onCreate() {
        startKoin(this, applicationModule, logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger())
        super.onCreate()
    }
}