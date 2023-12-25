package com.example.plantdisease.ui.screens.home

sealed class HomeUiAction {

    data class ActionWithParameter(val parameter: String) : HomeUiAction()

    object noParametrAction : HomeUiAction()

}
