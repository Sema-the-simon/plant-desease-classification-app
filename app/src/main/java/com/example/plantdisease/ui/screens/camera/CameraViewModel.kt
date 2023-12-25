package com.example.plantdisease.ui.screens.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantdisease.utils.convertToDateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: CameraUiAction) {
        when (action) {
            is CameraUiAction.TakePhoto ->
                viewModelScope.launch {
                    takePhoto(
                        action.context,
                        action.controller,
                        action.onPhotoTake
                    )
                }

            else -> {
                TODO()
            }
        }
    }


    private suspend fun takePhoto(
        context: Context,
        controller: LifecycleCameraController,
        onPhotoTake: (String) -> Unit
    ) {
        _uiState.update {
            uiState.value.copy(
                isCameraEnabled = false
            )
        }
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val bitmap = image.toBitmap()
                    val rotatedBitmap = Bitmap.createBitmap(
                        bitmap,
                        0, 0,
                        bitmap.width,
                        bitmap.height,
                        matrix,
                        true
                    )
                    viewModelScope.launch {
                        val uri = viewModelScope.async(Dispatchers.IO) {
                            saveToExternalStorage(
                                context,
                                "plant disease ${
                                    System.currentTimeMillis().convertToDateFormat()
                                }",
                                rotatedBitmap
                            )
                        }
                        onPhotoTake(uri.await())
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )

    }

    private suspend fun saveToExternalStorage(
        context: Context,
        imageName: String,
        bitmap: Bitmap,
    ): String {
        val imageCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$imageName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }

        return try {
            var encodedUrl = ""
            val resolver = context.contentResolver
            resolver.insert(imageCollection, contentValues)?.also { uri ->
                resolver.openOutputStream(uri).use { outStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outStream!!)) {
                        throw IOException("can't save bitmap")
                    }
                }
                encodedUrl = URLEncoder.encode("$uri", StandardCharsets.UTF_8.toString())

            } ?: throw IOException("can't create MediaStore entry")
            encodedUrl
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}

data class CameraUiState(
    val isCameraEnabled: Boolean = true,
)
