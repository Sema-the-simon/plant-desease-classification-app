package com.example.plantdisease.ui.screens.selected

import android.content.Context

sealed class SelectedImageUiAction {

    data class Analyze(val context: Context) : SelectedImageUiAction()

    object noParam : SelectedImageUiAction()

}
