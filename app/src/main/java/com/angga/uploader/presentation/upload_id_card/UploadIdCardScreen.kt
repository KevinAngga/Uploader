@file:OptIn(ExperimentalMaterial3Api::class)
import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.angga.uploader.presentation.components.uploader.UploaderRoot
import com.angga.uploader.presentation.components.uploader.UploaderState
import com.angga.uploader.presentation.ui.theme.UploaderTheme

@Composable
fun UploadIdCardScreenRoot(
    map : Map<String, UploaderState> = emptyMap(),
    openUploader : (documentType : String) -> Unit,
    cancelUploader : (documentType : String) -> Unit
) {
    ChangeStatusBar(useDarkIcon = true)
    UploadIdCardScreen(
        map = map,
        openUploader = openUploader,
        cancelUploader = cancelUploader
    )
}

@Composable
private fun UploadIdCardScreen(
    map : Map<String, UploaderState> = emptyMap(),
    openUploader : (documentType : String) -> Unit,
    cancelUploader : (documentType : String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        println("state "+map.getOrDefault("Uploader 1", UploaderState()).uri.path.toString())
        UploaderRoot(
            documentType = "Uploader 1",
            state = map.getOrDefault("Uploader 1", UploaderState()),
            onOpenCamera = {
                openUploader("Uploader 1")
            },
            cancelUpload = {
                cancelUploader("Uploader 1")
            }
        )

        Spacer(modifier = Modifier.width(24.dp))

        UploaderRoot(
            documentType = "Uploader 2",
            state = map.getOrDefault("Uploader 2", UploaderState()),
            onOpenCamera = {
                openUploader("Uploader 2")
            },
            cancelUpload = {
                cancelUploader("Uploader 2")
            }
        )
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
            openUploader = {},
            cancelUploader = {}
        )
    }
}