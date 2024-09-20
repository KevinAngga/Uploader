package com.angga.uploader.presentation.upload_id_card

sealed interface UploadIdCardEvent {
    data object GoToNext : UploadIdCardEvent
}