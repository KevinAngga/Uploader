package com.angga.uploader.presentation.upload_id_card

sealed interface UploadIdCardAction {
    data object onSubmitForm : UploadIdCardAction
    data class onIdCardUploadSuccess (val success : Boolean) : UploadIdCardAction
    data class onSelfieUploadSuccess (val success : Boolean) : UploadIdCardAction
}