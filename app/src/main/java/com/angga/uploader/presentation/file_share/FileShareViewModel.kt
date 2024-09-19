package com.angga.uploader.presentation.file_share

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FileShareViewModel : ViewModel() {

    var state by mutableStateOf(FileShareState())
        private set

    // Update the URI based on the documentType
    fun updateState(documentType: String, uri: Uri) {
        state = state.copy(
            uris = state.uris.toMutableMap().apply { put(documentType, uri) }
        )
    }

    // Retrieve the URI for a specific document type
    fun getUri(documentType: String): Uri {
        return state.uris[documentType] ?: Uri.EMPTY
    }

}