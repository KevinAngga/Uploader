package com.angga.uploader.presentation.components.uploader

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angga.uploader.domain.UploadRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import java.util.concurrent.CancellationException

class UploadViewModel(
    private val uploadRepository: UploadRepository
): ViewModel() {

    private val _listUploadState = MutableStateFlow<Map<String, UploaderState>>(emptyMap())
    val listUploadState = _listUploadState.asStateFlow()


    // Map to keep track of upload jobs
    private val uploadJobs = mutableMapOf<String, Job>()

    fun addUploadState(documentType: String, contentUri: Uri) {
        // Create a new UploadState
        val newUploadState = UploaderState(uri = contentUri, isUploading = false, canUpload = false)
        // Add the new upload task to the list
        _listUploadState.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[documentType] = newUploadState
            }
        }

        // Start the upload process
        startUpload(documentType, contentUri)
    }

    // Function to cancel an ongoing upload
    fun cancelUpload(documentType: String) {
        // Cancel the job associated with this indexId
        uploadJobs[documentType]?.cancel()
        uploadJobs.remove(documentType)

        //when the close click need to update the state
        _listUploadState.update {
            it.toMutableMap().apply {
                val state = this[documentType]
                if (state != null) {
                    this[documentType] = state.copy(
                        isUploading = false,
                        uri = Uri.EMPTY,
                        canUpload = true,
                        uploadFinish = false
                    )
                }
            }
        }
    }

    fun retryUpload(documentType: String) {
        _listUploadState.update {
            it.toMutableMap().apply {
                val state = this[documentType]
                if (state != null) {
                    this[documentType] = state.copy(
                        uploadError = false,
                    )
                }
            }
        }

        val currentState = _listUploadState.value[documentType]
        if (currentState != null) {
            startUpload(documentType =  documentType, currentState.uri)
        }
    }

    private fun startUpload(documentType: String, contentUri: Uri) {
        uploadJobs[documentType]?.cancel()

        val job = uploadRepository.uploadFiles(
            contentUri = contentUri,
            serverUrl = "https://dlptest.com/https-post/"
        ).onStart {
            _listUploadState.update {
                it.toMutableMap().apply {
                    val state = this[documentType]
                    if (state != null) {
                        this[documentType] = state.copy(
                            isUploading = true
                        )
                    }
                }
            }
        }.onEach {
            //only consume this when we need to have progressbar
        }.onCompletion { cause ->
            if (cause == null) {
                println("=== upload completed")
                _listUploadState.update {
                    it.toMutableMap().apply {
                        val state = this[documentType]
                        if (state != null) {
                            this[documentType] = state.copy(
                                isUploading = false,
                                uploadFinish = true
                            )
                        }
                    }

                }
            } else if (cause is CancellationException) {
                println("=== upload canceled")
                // Update the state to indicate the upload was cancelled
                _listUploadState.update {
                    it.toMutableMap().apply {
                        val state = this[documentType]
                        if (state != null) {
                            this[documentType] = state.copy(
                                isUploading = false,
                                uri = Uri.EMPTY,
                                canUpload = true,
                                uploadFinish = false
                            )
                        }
                    }
                }
            }
        }.catch {
            _listUploadState.update {
                it.toMutableMap().apply {
                    val state = this[documentType]
                    if (state != null) {
                        this[documentType] = state.copy(
                            isUploading = false,
                            uploadError = true,
                            uploadFinish = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

        uploadJobs[documentType] = job
    }

    override fun onCleared() {
        super.onCleared()
        println("==== upload view model cleared")
    }
}