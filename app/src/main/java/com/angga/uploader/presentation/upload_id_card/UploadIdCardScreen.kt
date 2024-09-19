@file:OptIn(ExperimentalMaterial3Api::class)
import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.angga.uploader.presentation.ui.theme.UploaderTheme

@Composable
fun UploadIdCardScreenRoot(
    uri: Uri = Uri.EMPTY,
    openUploader : () -> Unit
) {
    ChangeStatusBar(useDarkIcon = true)
    UploadIdCardScreen(
        uri = uri,
        openUploader = openUploader
    )
}

@Composable
private fun UploadIdCardScreen(
    uri: Uri = Uri.EMPTY,
    openUploader : () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        UploaderRoot(
            uri = uri,
            onOpenCamera = {
                openUploader()
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
        )
    }
}