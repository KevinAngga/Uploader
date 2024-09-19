package com.angga.uploader.presentation.components.uploader

import android.net.Uri

sealed interface UploadAction {
    data class StartUpload(val uri : Uri) : UploadAction
    data object CancelUpload : UploadAction
}