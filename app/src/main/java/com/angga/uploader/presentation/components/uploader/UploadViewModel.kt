package com.angga.uploader.presentation.components.uploader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UploadViewModel(
    private val documentType : String
) : ViewModel() {

    var state by mutableStateOf(UploaderState())
        private set

    init {
        println("==== instance "+documentType)
    }


    fun onAction(action: UploadAction) {
        when(action) {
            is UploadAction.StartUpload -> {
                state = state.copy(
                    uri = action.uri,
                    isUploading = true,
                    canUpload = false
                )
            }

            UploadAction.CancelUpload -> {
                state = state.copy(
                    isUploading = false,
                    canUpload = true,

                    )
            }
        }
    }
}