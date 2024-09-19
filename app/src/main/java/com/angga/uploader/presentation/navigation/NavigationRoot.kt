package com.angga.uploader.presentation.navigation

import CameraPreviewScreenRoot
import UploadIdCardScreenRoot
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.angga.uploader.presentation.file_share.FileShareViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named


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
        composable("upload_screen") { entry ->
            val sharedViewModel = entry
                .sharedViewModel<FileShareViewModel>(
                    viewModelQualifier = named("fileShareViewModel"),
                    navController = navController,
                    route = "upload"
                )
            UploadIdCardScreenRoot(
                uri = sharedViewModel.state.uri,
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
        composable("cameraX") { entry ->
            val sharedViewModel = entry
                .sharedViewModel<FileShareViewModel>(
                    viewModelQualifier = named("fileShareViewModel"),
                    navController = navController,
                    route = "upload"
                )

            CameraPreviewScreenRoot(
                cameraUsage = CameraUsage.PHOTO,
                onImageCallback = {
                    sharedViewModel.updateState(uri = it)
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    viewModelQualifier: Qualifier,
    navController: NavHostController,
    route : String? = null
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel(viewModelQualifier)
    val parentEntry = remember(this) {
        if (route != null) {
            navController.getBackStackEntry(route)
        } else {
            navController.getBackStackEntry(navGraphRoute)
        }
    }
    return koinViewModel(qualifier = viewModelQualifier, viewModelStoreOwner = parentEntry)
}