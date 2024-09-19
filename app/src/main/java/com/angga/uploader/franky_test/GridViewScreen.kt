package com.angga.uploader.franky_test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GridViewScreen(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(count = 3, key = {
            it
        }) { index ->
            ItemGrid(itemId = index, navController = navController)
        }
    }
}