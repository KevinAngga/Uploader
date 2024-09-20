package com.angga.uploader.presentation.components.uploader

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angga.uploader.domain.UploadRepository
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import java.io.FileNotFoundException
import java.util.concurrent.CancellationException

class UploadViewModel(
    private val uploadRepository: UploadRepository
): ViewModel() {

    private val _listUploadState = MutableStateFlow<Map<String, UploaderState>>(emptyMap())
    val listUploadState = _listUploadState.asStateFlow()


    // Map to keep track of upload jobs
    private val uploadJobs = mutableMapOf<String, Job>()

    fun addUploadState(documentType: String, imageUri: Uri) {
        // Create a new UploadState
        val newUploadState = UploaderState(uri = imageUri, isUploading = false, canUpload = false)
        // Add the new upload task to the list
        _listUploadState.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[documentType] = newUploadState
            }
        }

        // Start the upload process
        startUpload(documentType, imageUri)
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
        }.catch { cause ->
            val message = when(cause) {
                is OutOfMemoryError -> "File too large!"
                is FileNotFoundException -> "File not found!"
                is UnresolvedAddressException -> "No internet!"
                else -> "Something went wrong!"
            }
            println("=== upload error "+message)
        }.launchIn(viewModelScope)

        uploadJobs[documentType] = job
    }

    override fun onCleared() {
        super.onCleared()
        println("==== upload view model cleared")
    }
}