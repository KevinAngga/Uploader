package com.angga.uploader.presentation.camera

import android.net.Uri

sealed interface CameraAction {
    data object ChangeCamera : CameraAction
    data object TakePicture : CameraAction
    data object RecordVideo : CameraAction
    data object ChangeFlashSetting : CameraAction
    data class OnImageGalleryPick(val uri: Uri) : CameraAction
}