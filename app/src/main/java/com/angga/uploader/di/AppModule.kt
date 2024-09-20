package com.angga.uploader.di

import CameraViewModel
import com.angga.uploader.data.FileReader
import com.angga.uploader.data.HttpClientFactory
import com.angga.uploader.data.UploadRepositoryImpl
import com.angga.uploader.domain.UploadRepository
import com.angga.uploader.presentation.camera.CameraDataSource
import com.angga.uploader.presentation.components.uploader.UploadViewModel
import com.angga.uploader.presentation.upload_id_card.UploadIdCardViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val appHostModule = module {
    single { CameraDataSource(androidApplication().applicationContext) }
    single {
        HttpClientFactory().build("https://www.google.com")
    }
    single { FileReader(androidApplication().applicationContext) }
    singleOf(::UploadRepositoryImpl).bind<UploadRepository>()
    viewModelOf(::CameraViewModel)
    viewModelOf(::UploadIdCardViewModel)
    viewModel(named("UploadViewModel")) { UploadViewModel(get()) }
}