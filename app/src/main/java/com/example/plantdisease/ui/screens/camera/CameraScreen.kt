package com.example.plantdisease.ui.screens.camera

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    navigateToSelectedImage: (String) -> Unit,
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    val viewModel: CameraViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 40.dp, bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                CameraPreview(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(0.8f)

                )
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp)
                        .dashedBorder(4.dp, Color.Green)
                )
            }
        }

        IconButton(
            enabled = uiState.isButtonEnabled,
            onClick = {
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
                .scale(1.8f)
                .padding(bottom = 20.dp)
                .border(2.dp, Color.Black, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Take picture"
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 650)
@Composable
fun CameraScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    PlantDiseaseTheme(darkTheme = darkTheme) {
        CameraScreen { }
    }
}