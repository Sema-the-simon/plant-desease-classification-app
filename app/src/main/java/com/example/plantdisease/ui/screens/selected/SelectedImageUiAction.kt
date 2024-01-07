package com.example.plantdisease.ui.screens.selected

import android.content.Context

sealed class SelectedImageUiAction {
    data class Analyze(val context: Context, val string: String = "") : SelectedImageUiAction()
    data class StateChange(val state: ResultsStates) : SelectedImageUiAction()
}
