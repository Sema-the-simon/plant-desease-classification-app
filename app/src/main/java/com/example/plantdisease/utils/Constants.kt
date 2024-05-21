package com.example.plantdisease.utils

import androidx.compose.ui.graphics.Color

object Constants {
    const val MODEL_PATH = "yolo8_plant_disease.tflite"// "best_float32_v1.tflite"
    const val LABELS_PATH = "labels_v1_ru.txt"        // "labels_v1.txt"
    val COLORS = listOf(Color.Green, Color.Blue, Color.Magenta, Color.Cyan, Color.Red)
}
