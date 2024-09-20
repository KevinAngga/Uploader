package com.angga.uploader.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SubGraph {
    @Serializable
    data object MainScreen : SubGraph()

    @Serializable
    data object Uploader : SubGraph()

    @Serializable
    data object Camera : SubGraph()
}

@Serializable
sealed class Destination {
    @Serializable
    data object Main : Destination()

    @Serializable
    data object UploadIdCard : Destination()

    @Serializable
    data class CameraX (
        val documentType : String
    ): Destination()
}