package com.angga.uploader.presentation.camera

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.internal.CameraUseCaseAdapter.CameraException
import androidx.camera.video.VideoRecordEvent
import com.namea.domain.util.DataError

//should on data
fun handleVideoRecordError(errorCode : Int) : DataError.Camera {
    return when(errorCode) {
        VideoRecordEvent.Finalize.ERROR_FILE_SIZE_LIMIT_REACHED -> { DataError.Camera.FILE_SIZE_LIMIT_REACHED }
        VideoRecordEvent.Finalize.ERROR_INSUFFICIENT_STORAGE -> { DataError.Camera.INSUFFICIENT_STORAGE }
        VideoRecordEvent.Finalize.ERROR_INVALID_OUTPUT_OPTIONS -> { DataError.Camera.INVALID_OUTPUT_OPTIONS }
        VideoRecordEvent.Finalize.ERROR_ENCODING_FAILED -> { DataError.Camera.ENCODING_FAILED }
        VideoRecordEvent.Finalize.ERROR_RECORDER_ERROR -> { DataError.Camera.RECORDER_ERROR }
        VideoRecordEvent.Finalize.ERROR_NO_VALID_DATA -> { DataError.Camera.NO_VALID_DATA }
        VideoRecordEvent.Finalize.ERROR_SOURCE_INACTIVE -> { DataError.Camera.SOURCE_INACTIVE }
        else -> {
            DataError.Camera.UNKNOWN
        }
    }
}

fun handleTakePictureError(exception : ImageCaptureException) : DataError.Camera {
    return when(exception.imageCaptureError) {
        ImageCapture.ERROR_UNKNOWN -> {
            DataError.Camera.UNKNOWN
        }

        ImageCapture.ERROR_FILE_IO -> {
            DataError.Camera.ERROR_FIlE_IO
        }

        ImageCapture.ERROR_CAPTURE_FAILED -> {
            DataError.Camera.ERROR_CAPTURE_FAILED
        }

        ImageCapture.ERROR_CAMERA_CLOSED -> {
            DataError.Camera.ERROR_CAMERA_CLOSED
        }

        ImageCapture.ERROR_INVALID_CAMERA -> {
            DataError.Camera.ERROR_INVALID_CAMERA
        }

        else -> {
            DataError.Camera.UNKNOWN
        }
    }
}