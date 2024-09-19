package com.angga.uploader.franky_test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UploadState(
    val imageUri: String? = null,
    val isUploading: Boolean = false,
    val uploadProgress: Float = 0f,
)

class UploaderViewModel() : ViewModel() {
    private val _listUploadState = MutableStateFlow<Map<String, UploadState>>(emptyMap())
    val listUploadState = _listUploadState.asStateFlow()

    // Map to keep track of upload jobs
    private val uploadJobs = mutableMapOf<String, Job>()

    // Function to add a new upload task
    fun addUploadState(indexId: String, imageUri: String) {
        // Create a new UploadState
        val newUploadState = UploadState(imageUri = imageUri, isUploading = true)
        // Add the new upload task to the list
        _listUploadState.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[indexId] = newUploadState
            }
        }

        // Start the upload process
        startUpload(indexId, imageUri)
    }

    // Function to start the upload process
    private fun startUpload(indexId: String, imageUri: String) {
        // If an upload job already exists for this indexId, cancel it first
        uploadJobs[indexId]?.cancel()

        val job = viewModelScope.launch {
            var progress = 0f
            // Simulate upload process
            while (progress < 1f) {
                // Simulate some progress
                progress += 0.1f
                // Delay to simulate upload time
                delay(1500)

                _listUploadState.update {
                    it.toMutableMap().apply {
                        val state = this[indexId]

                        if (state != null) {
                            this[indexId] = state.copy(uploadProgress = progress)
                        }

                    }
                }
            }

            _listUploadState.update {
                it.toMutableMap().apply {
                    val state = this[indexId]
                    if (state != null) {
                        this[indexId] = state.copy(uploadProgress = 0f, isUploading = false, imageUri = null)
                    }

                }
            }
        }

        // Store the job in the map
        uploadJobs[indexId] = job
    }
}