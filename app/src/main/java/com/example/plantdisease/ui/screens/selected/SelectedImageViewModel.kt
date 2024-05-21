package com.example.plantdisease.ui.screens.selected

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantdisease.data.detector.BoundingBox
import com.example.plantdisease.data.detector.Detector
import com.example.plantdisease.data.navigation.SelectedImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.use
import javax.inject.Inject

@HiltViewModel
class SelectedImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectedImageUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: SelectedImageUiAction) {
        when (action) {
            is SelectedImageUiAction.Analyze -> viewModelScope.launch {
                _uiState.update {
                    uiState.value.copy(
                        isAnalyzing = true,
                        selectedProduct = action.string
                    )
                }
                analyzeImg(action.context, action.string)
            }

            is SelectedImageUiAction.StateChange -> {
                _uiState.update {
                    uiState.value.copy(
                        state = action.state
                    )
                }
            }
        }
    }

    init {
        val uri = savedStateHandle.get<String>(SelectedImage.uri) ?: ""
        _uiState.update {
            uiState.value.copy(
                uri = uri
            )
        }
    }

    private suspend fun analyzeImg(context: Context, product: String) {
        return withContext(Dispatchers.IO) {
            var bitmap: Bitmap? = null

            bitmap = viewModelScope.async(Dispatchers.IO) {
                context.contentResolver.openInputStream(
                    uiState.value.uri?.toUri() ?: throw IllegalArgumentException("URI was empty")
                ).use {
                    bitmap = BitmapFactory.decodeStream(it)
                }
                _uiState.update {
                    uiState.value.copy(
                        bitmap = bitmap
                    )
                }
                bitmap
            }.await()

            if (bitmap != null) {
                val detector = Detector(context)
                detector.setup()

                val res = detector.detect(bitmap!!)
                    //.filter { it.clsName.startsWith(product) }
                println()


                _uiState.update {
                    uiState.value.copy(
                        res = res,
                        isAnalyzing = false
                    )
                }
            }
        }
    }


}

data class SelectedImageUiState(
    val bitmap: Bitmap? = null,
    val uri: String? = null,
    val res: List<BoundingBox> = emptyList(),
    val isAnalyzing: Boolean = false,
    val state: ResultsStates = ResultsStates.IMG,
    val list: List<String> = listOf(
        "Apple Scab Leaf",
        "Apple rust leaf",
        "Bell_pepper leaf spot",
        "Corn Gray leaf spot",
        "Corn leaf blight",
        "Corn rust leaf",
        "Potato leaf early blight",
        "Potato leaf late blight",
        "Tomato Early blight leaf",
        "Tomato Septoria leaf spot",
        "Tomato leaf bacterial spot",
        "Tomato leaf late blight",
        "Tomato leaf mosaic virus",
        "Tomato leaf yellow virus",
        "Tomato mold leaf",
        "Tomato two spotted spider mites leaf",
        "grape leaf black rot"
    ),
    val selectedProduct: String = ""
)

enum class ResultsStates {
    IMG, LIST, RES
}
