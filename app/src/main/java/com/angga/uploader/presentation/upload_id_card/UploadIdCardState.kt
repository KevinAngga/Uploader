package com.angga.uploader.presentation.upload_id_card

data class UploadIdCardState(
    val isIdCardUploaded : Boolean = false,
    val isSelfieUploaded : Boolean = false,
    val isValidForm : Boolean = false,
)
