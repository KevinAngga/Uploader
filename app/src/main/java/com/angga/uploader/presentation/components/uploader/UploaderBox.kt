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
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun UploaderRoot(
    documentType: String,
    state: UploaderState,
    title: String = "Add Photo",
    onOpenCamera: () -> Unit,
    cancelUpload: () -> Unit,
    onUploadFinish : (documentType: String, uploadFinish : Boolean) -> Unit
) {

    LaunchedEffect(state.uploadFinish) {
        if (state.uploadFinish) {
            onUploadFinish(documentType, true)
        } else {
            onUploadFinish(documentType, false)
        }
    }

    UploaderBox(
        title = title,
        state = state,
        onOpenCamera = {
            onOpenCamera()
        },
        cancelUpload = {
            cancelUpload()
        }
    )
}

@Composable
fun UploaderBox(
    title: String = "Add Photo",
    onOpenCamera: () -> Unit,
    cancelUpload: () -> Unit,
    state: UploaderState
) {
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