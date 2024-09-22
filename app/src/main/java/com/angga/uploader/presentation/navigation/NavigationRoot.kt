package com.angga.uploader.presentation.navigation

import CameraPreviewScreenRoot
import CameraUsage
import UploadIdCardScreenRoot
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.angga.uploader.presentation.components.uploader.UploadViewModel
import com.angga.uploader.presentation.components.uploader.UploaderState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersHolder
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import kotlin.reflect.typeOf


@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = SubGraph.MainScreen
    ) {
        mainScreen(navController)
        uploadScreen(navController)
        cameraGraph(navController)
    }
}

private fun NavGraphBuilder.mainScreen(navController: NavHostController) {
    navigation<SubGraph.MainScreen>(
        startDestination = Destination.Main
    ) {
        composable<Destination.Main> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    onClick = { navController.navigate(SubGraph.Uploader) }
                ) {
                    Text(text = "Click Me")
                }
            }
        }
    }
}

private fun NavGraphBuilder.uploadScreen(navController: NavHostController) {
    navigation<SubGraph.Uploader>(
        startDestination = Destination.UploadIdCard,
    ) {
        composable<Destination.UploadIdCard> { entry ->
            val uploadViewModel = entry
                .sharedViewModel<UploadViewModel>(
                    viewModelQualifier = named("UploadViewModel"),
                    navController = navController,
                    route = SubGraph.Uploader::class.qualifiedName
                )

            val mapState by uploadViewModel.listUploadState.collectAsStateWithLifecycle()

            UploadIdCardScreenRoot(
                map = mapState,
                cancelUploader = {
                    uploadViewModel.cancelUpload(it)
                },
                openUploader = { documentType, cameraUsage ->
                    navController.navigate(
                        Destination.CameraX(
                            documentType = documentType,
                            cameraUsage = cameraUsage
                        )
                    )
                },
                retryUploader = {
                    uploadViewModel.retryUpload(it)
                },
                goToNext = {
                    navController.navigate(Destination.AfterUpload)
                }
            )
        }

        composable<Destination.AfterUpload> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Click Me")
            }
        }
    }
}


private fun NavGraphBuilder.cameraGraph(navController: NavHostController) {
    navigation<SubGraph.Camera>(
        startDestination = Destination.CameraX::class,
    ) {
        composable<Destination.CameraX>(
            typeMap = mapOf(
                typeOf<CameraUsage>() to NavType.EnumType(CameraUsage::class.java)
            )
        ) { entry ->
            val argument = entry.toRoute<Destination.CameraX>()
            val documentType = argument.documentType
            val cameraUsage = argument.cameraUsage

            val uploadViewModel = entry
                .sharedViewModel<UploadViewModel>(
                    viewModelQualifier = named("UploadViewModel"),
                    navController = navController,
                    route = SubGraph.Uploader::class.qualifiedName
                )

            CameraPreviewScreenRoot(
                cameraUsage = cameraUsage,
                onImageCallback = {
                    uploadViewModel.addUploadState(documentType = documentType, contentUri = it)
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
    route: String? = null,
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

//use this if the compose on the same navigation scope
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