package com.example.plantdisease.ui.screens.selected

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plantdisease.R
import com.example.plantdisease.ui.components.CustomTextButton

@Composable
fun SelectedImageScreen(

) {
    val context = LocalContext.current
    val viewModel: SelectedImageViewModel = hiltViewModel()
    val isBitmapReady by remember {
        mutableStateOf(viewModel.initViewModel(context = context))
    }
    val uiState by viewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 20.dp)
    ) {
        when (uiState.state) {
            ResultsStates.IMG -> {

                if (uiState.bitmap == null)
                    Image(
                        painterResource(id = R.drawable.image_loading),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Inside
                    )
                else
                    Image(
                        uiState.bitmap!!.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Inside
                    )

                CustomTextButton(
                    text = if (uiState.isAnalyzing) "Анализируем...." else "Анализировать",
                    onClick = {
                        viewModel.onUiAction(SelectedImageUiAction.StateChange(ResultsStates.LIST))
                    },
                    enabled = !uiState.isAnalyzing
                )

            }

            ResultsStates.LIST -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Выберете культуру, которую вы сфотографировали:",
                        modifier = Modifier.padding(bottom = 20.dp)
                    )


                    for (product in uiState.list) {
                        CustomTextButton(
                            text = product,
                            onClick = {
                                viewModel.onUiAction(
                                    SelectedImageUiAction.StateChange(ResultsStates.RES)
                                )
                                viewModel.onUiAction(SelectedImageUiAction.Analyze(context, product))

                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            ResultsStates.RES -> {
                Image(
                    uiState.bitmap!!.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Inside
                )

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "Результаты анализа:",
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    for (res in uiState.res) {
                        Text(text = "${(res.score * 100).toInt()}% - ${res.name}")
                    }
                }
            }
        }

    }
}