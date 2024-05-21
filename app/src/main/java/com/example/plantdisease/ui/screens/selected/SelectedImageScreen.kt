package com.example.plantdisease.ui.screens.selected

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.plantdisease.R
import com.example.plantdisease.ui.components.CustomTextButton
import com.example.plantdisease.utils.Constants
import kotlin.random.Random

@Composable
fun SelectedImageScreen(
    viewModel: SelectedImageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val colorsList = remember {
        Constants.COLORS//mutableListOf<Color>()
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 20.dp)
    ) {
        when (uiState.state) {
            ResultsStates.IMG -> {

                AsyncImage(
                    model = uiState.uri,
                    placeholder = painterResource(id = R.drawable.image_loading),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Inside
                )

                CustomTextButton(
                    text = if (uiState.isAnalyzing) "Анализируем...." else "Анализировать",
                    onClick = {
                        //viewModel.onUiAction(SelectedImageUiAction.StateChange(ResultsStates.LIST))
                        viewModel.onUiAction(
                            SelectedImageUiAction.Analyze(
                                context,
                                ""
                            )
                        )
                        viewModel.onUiAction(
                            SelectedImageUiAction.StateChange(ResultsStates.RES)
                        )
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
                                    SelectedImageUiAction.Analyze(
                                        context,
                                        product
                                    )
                                )
                                viewModel.onUiAction(
                                    SelectedImageUiAction.StateChange(ResultsStates.RES)
                                )

                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            ResultsStates.RES -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 20.dp, bottom = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.bitmap != null)
                        Image(
                            uiState.bitmap!!.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Inside
                        )
                    else
                        Image(
                            painterResource(id = R.drawable.image_loading),
                            contentDescription = "",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Inside
                        )


                    val res = uiState.res.groupBy { it.clsName }
                    val textMeasurer = rememberTextMeasurer()
                    var i = 0
                    res.forEach { (name, group) ->
                        val color = colorsList[i]
                        i++
                        for (i in group.indices) {
                            val el = group[i]
                            Canvas(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val height = size.height * 85 / 100
                                var width = size.width  * 95 / 100
                                var offset = size.width / 100 * 5

                                val left = width * el.x1 + offset
                                val top = height * el.y1 + offset
                                val right = width * el.x2 + offset
                                val bottom = height * el.y2 + offset

                                val measuredText =
                                    textMeasurer.measure(
                                        AnnotatedString("$i: "),
                                        style = TextStyle(
                                            color = Color.White, fontSize = 10.sp
                                        )
                                    )

                                val path = Path()
                                path.moveTo(left, top)
                                path.lineTo(right, top)
                                path.lineTo(right, bottom)
                                path.lineTo(left, bottom)
                                path.close()

                                drawRect(
                                    color,//Color.Black,
                                    Offset(x = left, y = top),
                                    Size(width * el.w, height * el.h),
                                    style = Stroke(width = 5f)
                                )

                                drawRect(
                                    color,
                                    Offset(x = left -5, y = top - 45),
                                    measuredText.size.toSize().times(1f)
                                )

                                drawText(
                                    measuredText,
                                    color = Color.Black,
                                    Offset(x = left, y = top - 50),
                                )

                            }
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Результаты анализа:",
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    var j = 0
                    for ((name, list) in uiState.res.groupBy { it.clsName }) {
                        val color = colorsList[j]
                        j++
                        val btmp = uiState.bitmap!!
                        Row {
                            Box(modifier = Modifier.background(color).width(20.dp).height(20.dp))
                            Text(text = " $name:")
                        }
                        for (i in list.indices) {
                            Text(text = "   $i: ${(list[i].cnf * 100).toInt()}%")
                        }


                    }
                }
            }
        }

    }
}