package com.angga.uploader

import android.app.Application
import com.angga.uploader.di.appHostModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UploaderApp :Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@UploaderApp)
            modules(
                appHostModule
            )
        }
    }
}