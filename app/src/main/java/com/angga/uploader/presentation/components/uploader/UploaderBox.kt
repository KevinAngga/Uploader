package com.angga.uploader.presentation.components.uploader

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.angga.uploader.presentation.CloseIcon
import com.angga.uploader.presentation.PlusIcon
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

@Composable
fun UploaderRoot(
    documentType: String,
    uri: Uri = Uri.EMPTY,
    title: String = "Add Photo",
    onOpenCamera: () -> Unit,
    cancelUpload: () -> Unit,
) {
    val koin = getKoin()
    val scope = remember {
        koin.getOrCreateScope("upload_scope_${documentType}", named<UploadViewModel>())
    }
    val uploadViewModel: UploadViewModel = remember {
        scope.get { parametersOf(documentType) }
    }

    UploaderBox(
        title = title,
        uri = uri,
        state = uploadViewModel.state,
        onOpenCamera = {
            onOpenCamera()
        },
        cancelUpload = {
            cancelUpload()
        },
        onAction = { action ->
            uploadViewModel.onAction(action)
        }
    )

    DisposableEffect(Unit) {
        println("==== dispose jalan")
        onDispose {
            scope.close()
        }
    }
}

@Composable
fun UploaderBox(
    uri: Uri = Uri.EMPTY,
    title: String = "Add Photo",
    onOpenCamera: () -> Unit,
    cancelUpload: () -> Unit,
    state: UploaderState,
    onAction: (UploadAction) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        if (uri != Uri.EMPTY) {
            onAction(UploadAction.StartUpload(uri))
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .border(
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent)
            .clip(RoundedCornerShape(8.dp))
    ) {
        if (!state.canUpload) {
            if (state.uri != Uri.EMPTY) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.uri)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Icon(
                        modifier = Modifier
                            .clickable {
                                onAction(UploadAction.CancelUpload)
                                cancelUpload()
                            }
                            .align(Alignment.TopEnd)
                            .padding(end = 9.dp, top = 9.dp),
                        imageVector = CloseIcon,
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    if (state.isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center),
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        if (state.canUpload) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        onOpenCamera()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = PlusIcon,
                    contentDescription = null,
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}