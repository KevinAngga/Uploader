package com.angga.uploader.presentation.components.uploader

import android.net.Uri

data class UploaderState(
    val uri : Uri = Uri.EMPTY,
    val canUpload : Boolean = true,
    val isUploading : Boolean = false,
    val uploadFinish : Boolean = false,
)