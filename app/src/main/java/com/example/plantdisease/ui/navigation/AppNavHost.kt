package com.example.plantdisease.ui.navigation

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.plantdisease.data.navigation.Camera
import com.example.plantdisease.data.navigation.Home
import com.example.plantdisease.data.navigation.SelectedImage
import com.example.plantdisease.ui.screens.camera.CameraScreen
import com.example.plantdisease.ui.screens.camera.CameraViewModel
import com.example.plantdisease.ui.screens.home.HomeScreen
import com.example.plantdisease.ui.screens.selected.SelectedImageScreen
import com.example.plantdisease.ui.screens.selected.SelectedImageViewModel

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
            val context = LocalContext.current
            val controller = remember {
                LifecycleCameraController(context).apply {
                    setEnabledUseCases(
                        CameraController.IMAGE_CAPTURE
                    )
                }
            }
            val cameraViewModel: CameraViewModel = hiltViewModel()
            CameraScreen(
                cameraViewModel,
                context,
                controller,
            ) { uri ->
                navController.navigate(SelectedImage.navToOrderWithArgs(uri))
            }
        }

        composable(SelectedImage.routeWithArgs, arguments = SelectedImage.arguments) {
            val selectedImageViewModel: SelectedImageViewModel = hiltViewModel()
            SelectedImageScreen(
                selectedImageViewModel
            )
        }

    }
}
