package com.angga.uploader.franky_test

import CameraPreviewScreenRoot
import CameraUsage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersHolder
import org.koin.core.parameter.parametersOf

@Composable
fun NavFrankyRoot(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "upload") {
        navigation(
            route = "upload",
            startDestination = "grid_view"
        ) {
            composable("grid_view") { backStackEntry ->
                val uploadViewModel = backStackEntry.koinSharedViewModel<UploaderViewModel>(key = null, navController = navController)
                val list by uploadViewModel.listUploadState.collectAsStateWithLifecycle()
                println("== grid_view uploadViewModel $uploadViewModel")
                GridViewScreen(navController, list = list)
            }

            composable(
                "cameraX/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0

                val uploadViewModel = backStackEntry.koinSharedViewModel<UploaderViewModel>(key = null, navController = navController)

                println("== cameraX uploadViewModel $uploadViewModel")

//                val previousBackStackEntry = remember(backStackEntry) {
//                    navController.previousBackStackEntry!!
//                }
//
//                //karena kita tahu sebelum ke camera preview, pasti akan ke gridview terlebih dahulu.
//                // sehingga kita bisa pastikan itemViewModelnya ini akan menggunakan viewmodel yang sama di GridItem
//                val itemViewModel = koinViewModel<ItemViewModel>(viewModelStoreOwner = previousBackStackEntry, key = itemId.toString())

//                //karena kita tahu sebelum ke camera preview, pasti akan ke gridview terlebih dahulu.
//                // sehingga kita bisa pastikan itemViewModelnya ini akan menggunakan viewmodel yang sama di GridItem
//                val itemViewModel = backStackEntry.koinSharedViewModel<ItemViewModel>(
//                    key = itemId.toString(),
//                    navController = navController
//                )

                CameraPreviewScreenRoot(
                    cameraUsage = CameraUsage.PHOTO,
                    onImageCallback = {
                        println("==== uploader" + it.path.toString())
                        uploadViewModel.addUploadState(itemId.toString(), it.path.toString())
//                        itemViewModel.savedStateHandle["capturedImageUri"] = it.path.toString()
                        navController.popBackStack()
                    }
                )
            }
        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.koinSharedViewModel(
    key: String?,
    params: ParametersHolder = parametersOf(),
    navController: NavController,
): T {
    val navGraphRoute =
        destination.parent?.route ?: return koinViewModel(key = key, parameters = { params })
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry, key = key, parameters = { params })
}