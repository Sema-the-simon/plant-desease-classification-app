package com.example.plantdisease.ui.screens.camera

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.unit.Dp

sealed class CameraUiAction {

    data class TakePhoto(
        val context: Context,
        val controller: LifecycleCameraController,
        val screenWidth: Dp,
        val onPhotoTake: (String) -> Unit
    ) : CameraUiAction()

}
