package com.example.plantdisease.data.classifier

import android.graphics.Bitmap
import com.example.plantdisease.data.model.DiseaseClassification

interface DiseaseClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<DiseaseClassification>
}