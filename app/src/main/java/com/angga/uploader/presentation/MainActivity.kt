package com.angga.uploader.presentation

import CameraPreviewScreenRoot
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.angga.uploader.franky_test.NavFrankyRoot
import com.angga.uploader.presentation.navigation.NavigationRoot
import com.angga.uploader.presentation.ui.theme.UploaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UploaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
//                    NavigationRoot(navController = navController)
                    NavFrankyRoot(navController = navController)
                }
            }
        }
    }
}
