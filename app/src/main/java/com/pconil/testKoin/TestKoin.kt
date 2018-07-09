package com.pconil.testKoin

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.log.EmptyLogger


open class TestKoin: Application() {

    override fun onCreate() {
        startKoin(this, applicationModules, logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger())
        super.onCreate()
    }
}