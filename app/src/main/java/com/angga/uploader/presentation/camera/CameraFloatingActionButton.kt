import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.angga.uploader.presentation.PlusIcon

@Composable
fun CameraFloatingActionButton(
    icon : ImageVector?,
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription : String? = null,
    iconSize : Dp = 25.dp
) {
    Box(
        modifier = modifier
            .size(75.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {}
    }
}


@Preview
@Composable
fun CameraFloatingActionButtonPreview() {
    CameraFloatingActionButton(
        icon = PlusIcon,
        onClick = {  }
    )
}