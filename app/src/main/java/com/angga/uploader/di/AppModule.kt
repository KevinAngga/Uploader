package com.angga.uploader.di

import CameraViewModel
import com.angga.uploader.franky_test.UploaderViewModel
import com.angga.uploader.presentation.camera.CameraDataSource
import com.angga.uploader.presentation.components.uploader.UploadViewModel
import com.angga.uploader.presentation.file_share.FileShareViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appHostModule = module {
    single { CameraDataSource(androidApplication().applicationContext) }
    viewModelOf(::CameraViewModel)
    viewModelOf(::UploadViewModel)
    viewModel(named("fileShareViewModel")) { FileShareViewModel() }

    viewModel { UploaderViewModel() }
}