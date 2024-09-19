package com.angga.uploader.presentation.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angga.uploader.presentation.PlusIcon

@Composable
fun UploaderBox(
    title: String = "Add Photo",
    canUpload : Boolean = true,
    isUploading : Boolean = false,
    onOpenCamera : () -> Unit
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
        if (isUploading) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.host_bg_image),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )


                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center),
                )
            }
        }

        if (canUpload) {
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
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 14.sp,
                )
            }
        }
    }
}