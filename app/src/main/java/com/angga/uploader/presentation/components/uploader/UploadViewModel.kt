package com.angga.uploader.presentation.components.uploader

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UploadViewModel: ViewModel() {

    private val _listUploadState = MutableStateFlow<Map<String, UploaderState>>(emptyMap())
    val listUploadState = _listUploadState.asStateFlow()

    fun addUploadState(documentType: String, imageUri: Uri) {
        // Create a new UploadState
        val newUploadState = UploaderState(uri = imageUri, isUploading = true, canUpload = false)
        // Add the new upload task to the list
        _listUploadState.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[documentType] = newUploadState
            }
        }

        // Start the upload process
//        startUpload(indexId, imageUri)
    }

    // Function to cancel an ongoing upload
    fun cancelUpload(documentType: String) {
        // Cancel the job associated with this indexId

        // Update the state to indicate the upload was cancelled
        _listUploadState.update {
            it.toMutableMap().apply {
                val state = this[documentType]
                if (state != null) {
                    this[documentType] = state.copy(
                        isUploading = false,
                        uri = Uri.EMPTY,
                        canUpload = true
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("==== upload view model cleared")
    }
}