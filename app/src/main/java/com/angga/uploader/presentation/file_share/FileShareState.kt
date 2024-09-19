package com.angga.uploader.presentation.file_share

import android.net.Uri

data class FileShareState(
    val uris: Map<String, Uri> = emptyMap()
)
