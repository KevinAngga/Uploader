@file:OptIn(ExperimentalMaterial3Api::class)
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.angga.uploader.presentation.ObserveAsEvents
import com.angga.uploader.presentation.components.uploader.UploaderRoot
import com.angga.uploader.presentation.components.uploader.UploaderState
import com.angga.uploader.presentation.ui.theme.UploaderTheme
import com.angga.uploader.presentation.upload_id_card.UploadIdCardAction
import com.angga.uploader.presentation.upload_id_card.UploadIdCardEvent
import com.angga.uploader.presentation.upload_id_card.UploadIdCardViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UploadIdCardScreenRoot(
    viewModel: UploadIdCardViewModel = koinViewModel(),
    map : Map<String, UploaderState> = emptyMap(),
    openUploader : (documentType : String, cameraUsage : CameraUsage) -> Unit,
    cancelUploader : (documentType : String) -> Unit,
) {
    val context = LocalContext.current

    ChangeStatusBar(useDarkIcon = true)

    ObserveAsEvents(flow = viewModel.event) { event ->
        when(event) {
            UploadIdCardEvent.GoToNext -> {
                Toast.makeText(
                    context,
                    "Sementara pake toast",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    UploadIdCardScreen(
        map = map,
        openUploader = openUploader,
        cancelUploader = cancelUploader,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun UploadIdCardScreen(
    map : Map<String, UploaderState> = emptyMap(),
    openUploader : (documentType : String, cameraUsage : CameraUsage) -> Unit,
    cancelUploader : (documentType : String) -> Unit,
    onAction : (UploadIdCardAction) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            UploaderRoot(
                documentType = "Uploader 1",
                state = map.getOrDefault("Uploader 1", UploaderState()),
                onOpenCamera = {
                    openUploader("Uploader 1", CameraUsage.SELFIE)
                },
                cancelUpload = {
                    cancelUploader("Uploader 1")
                },
                onUploadFinish = { documentType, uploadFinish ->
                    if (documentType == "Uploader 1") {
                        onAction(UploadIdCardAction.onIdCardUploadSuccess(uploadFinish))
                    }
                }
            )

            Spacer(modifier = Modifier.width(24.dp))

            UploaderRoot(
                documentType = "Uploader 2",
                state = map.getOrDefault("Uploader 2", UploaderState()),
                onOpenCamera = {
                    openUploader("Uploader 2", CameraUsage.PHOTO)
                },
                cancelUpload = {
                    cancelUploader("Uploader 2")
                },
                onUploadFinish = { documentType, uploadFinish ->
                    if (documentType == "Uploader 2") {
                        onAction(UploadIdCardAction.onSelfieUploadSuccess(uploadFinish))
                    }
                }
            )
        }

        Button(
            onClick = { onAction(UploadIdCardAction.onSubmitForm) }
        ) {
            Text(text = "Click Me")
        }
    }
}

@Composable
fun ChangeStatusBar(useDarkIcon : Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = Color.Transparent.toArgb()
            window.statusBarColor =  Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = useDarkIcon
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = useDarkIcon
        }
    }
}

@Preview
@Composable
private fun UploadIdCardScreenPreview() {
    UploaderTheme {
        UploadIdCardScreen(
            openUploader = { documentType, cameraUsage -> },
            cancelUploader = {},
            onAction = {}
        )
    }
}