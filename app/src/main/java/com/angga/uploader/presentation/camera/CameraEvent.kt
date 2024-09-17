import android.net.Uri

sealed interface CameraEvent {
    data class SuccessSubmitGetImageUri(val uri: Uri) : CameraEvent
}