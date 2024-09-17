package com.angga.uploader.presentation.navigation

import CameraPreviewScreenRoot
import UploadIdCardScreenRoot
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation


@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "upload"
    ) {
        uploadScreen(navController)
        cameraGraph(navController)
    }
}


private fun NavGraphBuilder.uploadScreen(navController: NavHostController) {
    navigation(
        startDestination = "upload_screen",
        route = "upload"
    ) {
        composable("upload_screen") {
            UploadIdCardScreenRoot(
                openUploader = {
                    navController.navigate("camera")
                }
            )
        }
    }
}


private fun NavGraphBuilder.cameraGraph(navController: NavHostController) {
    navigation(
        startDestination = "cameraX",
        route = "camera"
    ) {
        composable("cameraX") {
            CameraPreviewScreenRoot(
                cameraUsage = CameraUsage.PHOTO,
                onImageCallback = {
                    println("==== uploader"+it.path.toString())
                }
            )
        }
    }
}