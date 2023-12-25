package com.example.plantdisease.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.plantdisease.ui.theme.ExtendedTheme
import com.example.plantdisease.ui.theme.PlantDiseaseTheme
import com.example.plantdisease.ui.theme.ThemeModePreview

@Composable
fun CustomTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled:Boolean = true,
    containerColor: Color = ExtendedTheme.colors.backPrimary,
    contentColor: Color = ExtendedTheme.colors.labelPrimary,
    borderColor: Color = Color.Blue
) {
    TextButton(
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(width = 1.dp, color = borderColor),
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Preview()
@Composable
fun ButtonPreview(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    PlantDiseaseTheme(
        darkTheme = darkTheme
    ) {
        Column {
            CustomTextButton("button text", {})
            CustomTextButton("short", {})
            CustomTextButton("very very long looong", {})
        }
    }
}