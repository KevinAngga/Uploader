package com.angga.uploader.presentation.navigation

import CameraPreviewScreenRoot
import UploadIdCardScreenRoot
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
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
        startDestination = SubGraph.Uploader
    ) {

        uploadScreen(navController)
        cameraGraph(navController)

//        composable<Destinations.CameraX> { entry ->
//            val argument = entry.toRoute<Destinations.CameraX>()
//            val documentType = argument.documentType
//            CameraPreviewScreenRoot(
//                cameraUsage = CameraUsage.PHOTO,
//                onImageCallback = {
//                    sharedViewModel.updateState(documentType =  documentType, uri = it)
//                    navController.popBackStack()
//                }
//            )
//        }
    }
}


private fun NavGraphBuilder.uploadScreen(navController: NavHostController) {
    navigation<SubGraph.Uploader>(
        startDestination = Destination.UploadIdCard,
    ) {
        composable<Destination.UploadIdCard> { entry ->
//            val sharedViewModel = entry
//                .sharedViewModel<FileShareViewModel>(
//                    viewModelQualifier = named("fileShareViewModel"),
//                    navController = navController,
//                    route = SubGraph.Uploader::class.qualifiedName
//                )

            UploadIdCardScreenRoot(
                idCardUri = Uri.EMPTY,
                selfieUri = Uri.EMPTY,
                cancelUploader = {
//                    sharedViewModel.updateState(documentType = it, uri = Uri.EMPTY)
                },
                openUploader = {
                    println("==== documentType "+it)
                    navController.navigate(
                        Destination.CameraX(
                            documentType = it
                        )
                    )
                }
            )
        }
    }
}


private fun NavGraphBuilder.cameraGraph(navController: NavHostController) {
    navigation<SubGraph.Camera>(
        startDestination = Destination.CameraX,
    ) {
        composable<Destination.CameraX> { entry ->
            val argument = entry.toRoute<Destination.CameraX>()
            val documentType = "Uploader 1"
//            val sharedViewModel = entry
//                .sharedViewModel<FileShareViewModel>(
//                    viewModelQualifier = named("fileShareViewModel"),
//                    navController = navController,
//                    route = SubGraph.Uploader::class.qualifiedName
//                )

            CameraPreviewScreenRoot(
                cameraUsage = CameraUsage.PHOTO,
                onImageCallback = {
//                    sharedViewModel.updateState(documentType =  documentType, uri = it)
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