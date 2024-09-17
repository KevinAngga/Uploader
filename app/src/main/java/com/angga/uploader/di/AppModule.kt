package com.angga.uploader.di

import CameraViewModel
import com.angga.uploader.presentation.camera.CameraDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appHostModule = module {
    single { CameraDataSource(androidApplication().applicationContext) }
    viewModelOf(::CameraViewModel)
}