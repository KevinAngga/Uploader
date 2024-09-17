import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angga.uploader.presentation.camera.CameraAction
import com.namea.domain.util.Result
import com.angga.uploader.presentation.camera.CameraDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CameraViewModel (
    private val cameraDataSource: CameraDataSource
) : ViewModel() {

    val cameraController = cameraDataSource.getCameraController()

    var state by mutableStateOf(CameraState())
        private set

    private val eventChannel = Channel<CameraEvent>()
    val event = eventChannel.receiveAsFlow()

    fun onAction(action: CameraAction) {
        when(action) {
            CameraAction.ChangeCamera -> {
                changeCamera()
            }

            CameraAction.TakePicture -> {
                takePicture()
            }

            CameraAction.RecordVideo -> {
                if (state.isRecording) {
                    cameraDataSource.stopRecording()
                } else {
                    captureVideo()
                }
            }

            CameraAction.ChangeFlashSetting -> {
                changeFlashSettings()
            }

            is CameraAction.OnImageGalleryPick -> {
                viewModelScope.launch {
                    if (action.uri != Uri.EMPTY) {
                        eventChannel.send(CameraEvent.SuccessSubmitGetImageUri(action.uri))
                    }
                }
            }

            else -> {}
        }
    }

    private fun changeCamera() {
        if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    private fun changeFlashSettings() {
        when (state.cameraFlash) {
            CameraFlash.AUTO -> {
                state = state.copy(cameraFlash = CameraFlash.FLASH_ON)
                cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_ON
            }

            CameraFlash.FLASH_ON -> {
                state = state.copy(cameraFlash = CameraFlash.FLASH_OFF)
                cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_OFF
            }

            CameraFlash.FLASH_OFF -> {
                state = state.copy(cameraFlash = CameraFlash.AUTO)
                cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_AUTO
            }
        }
    }

    private fun captureVideo() {
        state = state.copy(isRecording = true)
        viewModelScope.launch {
            cameraDataSource.captureVideo().collect { result ->
                when (result) {
                    is Result.Failed -> {
                        state = state.copy(
                            isRecording = false
                        )
                        println("==== error "+result.error.name)
                    }
                    is Result.Success -> {
                        state = state.copy(
                            isRecording = false
                        )

                        eventChannel.send(CameraEvent.SuccessSubmitGetImageUri(result.data))
                        println("==== sukses "+result.data.path.toString())
                    }
                }
            }
        }
    }

    private fun takePicture() {
        viewModelScope.launch {
            cameraDataSource.captureImage().collect { result ->
                when(result) {
                    is  Result.Success -> {
                        println("==== sukses "+result.data.path.toString())
                        eventChannel.send(CameraEvent.SuccessSubmitGetImageUri(result.data))
                    }

                    is Result.Failed -> {
                        println("==== error "+result.error.name)
                    }
                }
            }
        }
    }
}