package com.angga.uploader.presentation.file_share

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FileShareViewModel : ViewModel() {

    var state by mutableStateOf(FileShareState())
        private set


    fun updateState(uri : Uri) {
        state = state.copy(
            uri = uri
        )
    }

}