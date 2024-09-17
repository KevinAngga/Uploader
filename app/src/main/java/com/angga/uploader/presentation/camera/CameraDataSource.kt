package com.angga.uploader.presentation.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import com.namea.domain.util.DataError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.namea.domain.util.Result

class CameraDataSource(
    private val context: Context,
) {
    private val cameraController = LifecycleCameraController(context)

    val outputFile = File(context.filesDir, "video.mp4")
    val outputOptions = FileOutputOptions.Builder(outputFile).build()


    private var recording: Recording? = null

    @SuppressLint("MissingPermission")
    fun captureVideo(): Flow<Result<Uri, DataError.Camera>> = callbackFlow {
        cameraController.setEnabledUseCases(CameraController.VIDEO_CAPTURE)

        if (recording != null) {
            recording?.stop()
            recording = null
        }

        recording = cameraController.startRecording(
            outputOptions,
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(context)
        ) { event ->
            when (event) {
                is VideoRecordEvent.Finalize -> {
                    if (event.hasError()) {
                        recording?.close()
                        trySend(Result.Failed(handleVideoRecordError(event.error)))
                    } else {
                        val outputUri = event.outputResults.outputUri
                        trySend(Result.Success(outputUri))
                    }

                    close()
                }
            }
        }

        // Await close from the caller side
        awaitClose {
            stopRecording()
        }
    }

    @SuppressLint("MissingPermission")
    fun captureImage(): Flow<Result<Uri, DataError.Camera>> = callbackFlow {
        if (!cameraController.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
            trySend(Result.Failed(DataError.Camera.ERROR_CAMERA_CLOSED))
            return@callbackFlow
        }

        cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE)

//        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        cameraController.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    Log.e("CameraDataSource", "Image capture success")

                    val savedUri = saveImageToFile(image)
                    image.close()

                    if (savedUri != null) {
                        Log.e("CameraDataSource", "Image saved successfully: $savedUri")
                        trySend(Result.Success(savedUri))
                    } else {
                        Log.e("CameraDataSource", "Image saving failed.")
                        trySend(Result.Failed(DataError.Camera.ERROR_FIlE_IO))
                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraDataSource", "Image capture failed: ${exception.message}")
                    trySend(Result.Failed(handleTakePictureError(exception)))
                    close()
                }
            }
        )

        awaitClose { }
    }

    fun stopRecording() {
        recording?.stop()
        recording?.close()
    }

    fun getCameraController() = cameraController

    private fun saveImageToFile(image: ImageProxy): Uri? {
        // Convert ImageProxy to Bitmap
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        // Save bitmap to file
        return try {
//            val photoFile = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")
            val photoFile = File(context.filesDir, "photo.jpg")
            val outputStream = FileOutputStream(photoFile)

            // Compress the bitmap into JPEG and save to the file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            outputStream.flush()
            outputStream.close()

            // Return the Uri of the saved file
            Uri.fromFile(photoFile)
        } catch (e: IOException) {
            Log.e("CameraDataSource", "Error saving image: ${e.message}")
            null
        }
    }
}