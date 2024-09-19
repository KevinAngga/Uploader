package com.angga.uploader.franky_test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun ItemGrid(itemId: Int, navController: NavController, uploadState: UploadState?) {
//    val itemViewModel: ItemViewModel =
//        koinViewModel(key = itemId.toString()) { parametersOf(itemId) }

//    val itemViewModel = navController.currentBackStackEntry!!.koinSharedViewModel<ItemViewModel>(
//        key = itemId.toString(),
//        navController = navController
//    )

//    // Observe the imageUri StateFlow
//    val imageUri by itemViewModel.imageUri.collectAsStateWithLifecycle()
//
//    // Collect the uploadProgress StateFlow
//    val uploadProgress by itemViewModel.uploadProgress.collectAsStateWithLifecycle()
//
//    val stillUpload by itemViewModel.stillUpload.collectAsStateWithLifecycle()
//
//    LaunchedEffect(key1 = imageUri, key2 = stillUpload) {
//        imageUri?.let { uri ->
//            if (uri.isNotEmpty() && !stillUpload) {
//                itemViewModel.uploadImage()
//            }
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DummyUploaderBox(onOpenCamera = {
            navController.navigate("cameraX/$itemId")
        })

        uploadState?.let { uploadState ->
            if (uploadState.uploadProgress > 0f && uploadState.uploadProgress < 1f) {
                LinearProgressIndicator(
                    progress = { uploadState.uploadProgress },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(8.dp),
                    color = Color.Green,
                )
                Text(text = "Uploading: ${(uploadState.uploadProgress * 100).toInt()}%")
            }
        }

    }
}