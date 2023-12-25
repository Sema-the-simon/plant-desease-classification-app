package com.example.plantdisease.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.plantdisease.ui.components.CustomTextButton
import com.example.plantdisease.ui.theme.PlantDiseaseTheme
import com.example.plantdisease.ui.theme.ThemeModePreview
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(
    navigateHomeToCamera: () -> Unit,
    navigateToSelectedImage: (String) -> Unit
) {
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val encodedUrl = URLEncoder.encode("$uri", StandardCharsets.UTF_8.toString())
        navigateToSelectedImage(encodedUrl)
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Предоставьте изображение")
        }

        CustomTextButton(
            text = "С камеры",
            onClick = navigateHomeToCamera
        )
        CustomTextButton(
            text = "С устройства",
            onClick = {
                photoLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 650)
@Composable
fun HomeScreenPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    PlantDiseaseTheme(darkTheme = darkTheme) {
        HomeScreen(
            {},
            {}
        )
    }
}