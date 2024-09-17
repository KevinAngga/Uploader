import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.angga.uploader.presentation.CloseIcon
import com.angga.uploader.presentation.FlipCameraIcon
import com.angga.uploader.presentation.GalleryIcon
import com.angga.uploader.presentation.ObserveAsEvents
import com.angga.uploader.presentation.camera.CameraAction
import com.angga.uploader.presentation.ui.theme.UploaderTheme
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun CameraPreviewScreenRoot(
    cameraViewModel: CameraViewModel = koinViewModel(),
    cameraUsage: CameraUsage = CameraUsage.PHOTO,
    onImageCallback : (uri : Uri) -> Unit,
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    ObserveAsEvents(flow = cameraViewModel.event) { event ->
        when(event) {
            is CameraEvent.SuccessSubmitGetImageUri -> {
                onImageCallback(event.uri)
            }

            else -> {}
        }
    }

    CameraPreview(
        modifier = modifier,
        controller = cameraViewModel.cameraController,
        state = cameraViewModel.state,
        cameraUsage = cameraUsage,
        onAction = { action ->
            cameraViewModel.onAction(action)
        }
    )
}

@Composable
fun CameraPreview(
    modifier: Modifier,
    controller: LifecycleCameraController?,
    cameraUsage : CameraUsage = CameraUsage.PHOTO,
    state: CameraState,
    onAction: (CameraAction) -> Unit,
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    //this can be deleted
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contentUri ->
        contentUri?.let { uri ->
            Timber.e("=== uri "+uri.path.toString())
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { grantedMap ->
            val allGranted = grantedMap.values.all { it }
            if (allGranted) {
                // Permission is granted
            } else {
                // Permission is denied
            }
        }

    // Create a launcher for the gallery intent
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onAction(CameraAction.OnImageGalleryPick(result.data?.data ?: Uri.EMPTY))
        }
    }


    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        )
    }
    
    CameraPreviewStatusBar()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        // Top section with title and instructions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "close",
                        tint = Color.Unspecified
                    )
                }

                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { onAction(CameraAction.ChangeFlashSetting) }
                ) {
                    Icon(
                        imageVector = ImageVector
                            .vectorResource(id = state.cameraFlash.changeFlashIcon()),
                        contentDescription = "flash",
                        tint = Color.Unspecified
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = stringResource(id = cameraUsage.setTitle()),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                text = stringResource(id = cameraUsage.setDescription()),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }

        // Camera preview section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(vertical = 24.dp),
                factory = { context ->
                    PreviewView(context).apply {
                        this.controller = controller
                        controller?.bindToLifecycle(lifeCycleOwner)
                    }
                }
            )
        }

        // Bottom section with controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        onAction(CameraAction.ChangeCamera)
                    }
                ) {
                    Icon(
                        imageVector = FlipCameraIcon,
                        contentDescription = "flip",
                        tint = Color.Unspecified
                    )
                }

                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        val intent = Intent(Intent.ACTION_PICK).apply {
                            type = "image/*"
                        }
                        galleryLauncher.launch(intent)
                    }
                ) {
                    Icon(
                        imageVector = GalleryIcon,
                        contentDescription = "gallery",
                        tint = Color.Unspecified
                    )
                }
            }

            CameraFloatingActionButton(
                icon = null,
                onClick = {
                    if (cameraUsage == CameraUsage.RECORD) {
                        onAction(CameraAction.RecordVideo)
                    } else {
                        onAction(CameraAction.TakePicture)
                    }
                }
            )
        }
    }
}

@Composable
fun CameraPreviewStatusBar() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = Color.Black.toArgb()
            window.statusBarColor = Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }
}

@Preview
@Composable
fun CameraPreviewPrev() {
    UploaderTheme {
        CameraPreview(
            modifier = Modifier,
            controller = null,
            state = CameraState(),
            onAction = {}
        )
    }
}