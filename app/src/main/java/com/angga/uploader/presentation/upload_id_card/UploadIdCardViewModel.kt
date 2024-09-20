package com.angga.uploader.presentation.upload_id_card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UploadIdCardViewModel : ViewModel() {

    var state by mutableStateOf(UploadIdCardState())
        private set

    private val eventChannel = Channel<UploadIdCardEvent>()
    val event = eventChannel.receiveAsFlow()

    fun onAction(action: UploadIdCardAction) {
        when(action) {
            UploadIdCardAction.onSubmitForm -> {
                state = state.copy(
                    isValidForm = state.isIdCardUploaded && state.isSelfieUploaded
                )

                if (state.isValidForm) {
                    viewModelScope.launch {
                        eventChannel.send(UploadIdCardEvent.GoToNext)
                    }
                }
            }

            is UploadIdCardAction.onIdCardUploadSuccess -> {
                state = state.copy(
                    isIdCardUploaded = action.success
                )
            }
            is UploadIdCardAction.onSelfieUploadSuccess -> {
                state = state.copy(
                    isSelfieUploaded = action.success
                )
            }
        }
    }
}