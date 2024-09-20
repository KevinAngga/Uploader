package com.angga.uploader.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface UploadRepository {
    fun uploadFiles(contentUri : Uri, serverUrl : String) : Flow<ProgressUpdate>
}