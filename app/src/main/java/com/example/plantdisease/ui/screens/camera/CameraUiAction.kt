package com.example.plantdisease.ui.screens.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController

sealed class CameraUiAction {

    data class TakePhoto(
        val context: Context,
        val controller: LifecycleCameraController,
        val onPhotoTake: (String) -> Unit
    ) : CameraUiAction()

    object noParametrAction : CameraUiAction()

}
