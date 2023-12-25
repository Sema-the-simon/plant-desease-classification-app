package com.example.plantdisease.ui.screens.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = _uiState.asStateFlow()
    fun onUiAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.ActionWithParameter -> TODO()
            HomeUiAction.noParametrAction -> TODO()
        }
    }
}

data class ListUiState(
     val value: Int = 0,
)
