package com.angga.uploader.franky_test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

//class ItemViewModel(
//    val savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    private val _imageUri = MutableStateFlow<String?>(null)
//    val imageUri: StateFlow<String?> = _imageUri
//
//    init {
//        savedStateHandle.getStateFlow("capturedImageUri", "")
//            .onEach { result ->
//                setImageUri(result)
//            }
//            .launchIn(viewModelScope)
//    }
//
//
//    private val _uploadProgress = MutableStateFlow(0f)
//    val uploadProgress: StateFlow<Float> = _uploadProgress.asStateFlow()
//
//    private val _stillUpload = MutableStateFlow(false)
//    val stillUpload: StateFlow<Boolean> = _stillUpload.asStateFlow()
//
//    private fun setImageUri(uri: String) {
//        _imageUri.value = uri
//    }
//
//    fun uploadImage() {
//        viewModelScope.launch {
//            _stillUpload.value = true
//            _uploadProgress.value = 0f
//            delay(500)
//            _uploadProgress.value += 0.1f
//
//            while (_uploadProgress.value < 1f) {
//                delay(1000)
//                _uploadProgress.value += 0.1f
//            }
//            _uploadProgress.value = 1f
//            _imageUri.value = null
//            _stillUpload.value = false
//        }
//    }
//}