package com.example.plantdisease.ui.screens.camera

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plantdisease.ui.components.camera.CameraPreview
import com.example.plantdisease.ui.theme.PlantDiseaseTheme
import com.example.plantdisease.ui.theme.ThemeModePreview
import com.example.plantdisease.utils.extentions.dashedBorder

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    context: Context? = null,
    controller: LifecycleCameraController? = context?.let { LifecycleCameraController(it) },
    navigateToSelectedImage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 20.dp)
    ) {
        Text(
            text = "Сделайте фото растения в выделенной области",
            color = Color.Green,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.Gray.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(8.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(top = 20.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (controller != null && uiState.isCameraEnabled)
                CameraPreview(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxSize()
                    //.border(5.dp, Color.Red)
                )
            if (uiState.isImageSaving) {
                Text(
                    text = "Сохраняем...",
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                        .background(Color.DarkGray.copy(alpha = 0.8f))
                        .align(Alignment.TopCenter)
                )
            }

            Box(
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
                    .dashedBorder(4.dp, Color.Green)
            )
        }
        OutlinedIconButton(
            enabled = !uiState.isImageSaving,
            onClick = {
                if (context != null && controller != null)
                    viewModel.onUiAction(
                        CameraUiAction.TakePhoto(
                            context,
                            controller
                        ) { uri ->
                            navigateToSelectedImage(uri)
                        }
                    )
            },
            modifier = Modifier
                .scale(2f)
                .padding(bottom = 20.dp)

        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Take picture"
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 780)
@Composable
fun CameraScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    PlantDiseaseTheme(darkTheme = darkTheme) {
        CameraScreen() { }
    }
}