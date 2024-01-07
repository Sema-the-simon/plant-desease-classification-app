package com.example.plantdisease.ui.screens.selected

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantdisease.data.classifier.TFDiseaseClassifier
import com.example.plantdisease.data.model.DiseaseClassification
import com.example.plantdisease.data.navigation.SelectedImage
import com.example.plantdisease.ui.components.centerCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.use
import javax.inject.Inject

class SelectedImageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
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

    fun initViewModel(context: Context): Boolean {
        val uri = savedStateHandle.get<String>(SelectedImage.uri) ?: ""
        var bitmap: Bitmap? = null

        viewModelScope.launch(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri.toUri()).use {
                bitmap = BitmapFactory.decodeStream(it).centerCrop(3000, 3000)
            }
            _uiState.update {
                uiState.value.copy(
                    bitmap = bitmap
                )
            }
        }
        return bitmap != null
    }

    private suspend fun analyzeImg(context: Context, product: String) {
        return withContext(Dispatchers.IO) {
            val btm = uiState.value.bitmap
            if (btm != null)
                _uiState.update {
                    uiState.value.copy(
                        res = TFDiseaseClassifier(context)
                            .classify(btm, 0)
                            .sortedByDescending { it.score }
                            .filter { it.name.startsWith(product) },
                        isAnalyzing = false
                    )
                }
        }
    }


}

data class SelectedImageUiState(
    val bitmap: Bitmap? = null,
    val res: List<DiseaseClassification> = emptyList(),
    val isAnalyzing: Boolean = false,
    val state: ResultsStates = ResultsStates.IMG,
    val list: List<String> = listOf(
        "Cabbage",//Cabbage - Капуста
        "Tomato",//Tomato - Помидор
        "Soy",//Soy - Соя
        "Maize"//Maize - Кукуруза
    ),
    val selectedProduct: String = ""
)

enum class ResultsStates {
    IMG, LIST, RES
}