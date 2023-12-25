package com.example.plantdisease.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.plantdisease.data.navigation.Camera
import com.example.plantdisease.data.navigation.Home
import com.example.plantdisease.data.navigation.SelectedImage
import com.example.plantdisease.ui.screens.camera.CameraScreen
import com.example.plantdisease.ui.screens.home.HomeScreen
import com.example.plantdisease.ui.screens.selected.SelectedImageScreen

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home.route
    ) {

        composable(Home.route) {
            HomeScreen(
                navigateHomeToCamera = {
                    navController.navigate(Camera.route)
                },
                navigateToSelectedImage = { uri ->
                    navController.navigate(SelectedImage.navToOrderWithArgs(uri))
                }
            )
        }

        composable(Camera.route) {
            CameraScreen { uri ->
                navController.navigate(SelectedImage.navToOrderWithArgs(uri))
            }
        }

        composable(
            SelectedImage.routeWithArgs,
            arguments = SelectedImage.arguments
        ) {
            SelectedImageScreen()
        }

    }
}
