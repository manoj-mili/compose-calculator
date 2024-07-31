package com.mili.simplecalculator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mili.simplecalculator.domain.model.CalculatorInput
import com.mili.simplecalculator.ui.components.BracketButton
import com.mili.simplecalculator.ui.components.ClearButton
import com.mili.simplecalculator.ui.components.NumberButton
import com.mili.simplecalculator.ui.components.OperatorButton
import com.mili.simplecalculator.ui.components.SeparatorButton
import com.mili.simplecalculator.ui.theme.black
import com.mili.simplecalculator.ui.theme.fontFamily
import com.mili.simplecalculator.ui.theme.lowEmphasisButtonDarkTheme
import com.mili.simplecalculator.ui.theme.lowEmphasisButtonLightTheme
import com.mili.simplecalculator.ui.theme.mediumEmphasisButtonDarkTheme
import com.mili.simplecalculator.ui.theme.mediumEmphasisButtonLightTheme
import com.mili.simplecalculator.ui.theme.purple
import com.mili.simplecalculator.ui.theme.white

@Composable
fun CalculatorScreen(uiState: CalculatorUiState, uiEvent: CalculatorUiEvent) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.wrapContentSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
            ) {
                BoxWithConstraints {
                    TextField(
                        value = uiState.input,
                        onValueChange = {
                            /**
                             no-op as the input is passed to vm to process
                             **/
                        },
                        maxLines = 2,
                        textStyle = TextStyle(
                            color = if (isSystemInDarkTheme()) white else black,
                            fontSize = calculateShrunkFontSize(uiState.input),
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.End
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedTextColor = if (isSystemInDarkTheme()) {
                                mediumEmphasisButtonDarkTheme
                            } else {
                                mediumEmphasisButtonLightTheme
                            }
                        )
                    )
                }

                if (uiState.result.isNotEmpty()) {
                    Text(
                        uiState.result,
                        textAlign = TextAlign.End,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .align(Alignment.End)
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(bottom = 16.dp, top = 64.dp),
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.BottomCenter)
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .background(Color.Black)
        ) {
            itemsIndexed(uiState.buttons) { index, item ->
                val color: Color = if (index + 1 < 4) {
                    if (isSystemInDarkTheme()) {
                        mediumEmphasisButtonDarkTheme
                    } else {
                        mediumEmphasisButtonLightTheme
                    }
                } else if ((index + 1) % 4 == 0) {
                    purple
                } else {
                    if (isSystemInDarkTheme()) {
                        lowEmphasisButtonDarkTheme
                    } else {
                        lowEmphasisButtonLightTheme
                    }
                }
                when (item) {
                    is CalculatorInput.ClearInput -> {
                        ClearButton(
                            operatorOption = item,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(8.dp),
                            color = color
                        ) { option ->
                            uiEvent.onCalculatorButtonClicked(option)
                        }
                    }

                    is CalculatorInput.OperatorInput -> {
                        OperatorButton(
                            operatorOption = item,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(8.dp),
                            color = color
                        ) { option ->
                            uiEvent.onCalculatorButtonClicked(option)
                        }
                    }

                    is CalculatorInput.NumberInput -> {
                        NumberButton(
                            numberOption = item,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(8.dp),
                            color = color
                        ) { option ->
                            uiEvent.onCalculatorButtonClicked(option)
                        }
                    }

                    is CalculatorInput.SeparatorInput -> {
                        SeparatorButton(
                            separatorOption = item,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(8.dp),
                            color = color
                        ) { option ->
                            uiEvent.onCalculatorButtonClicked(option)
                        }
                    }

                    is CalculatorInput.BracketInput -> {
                        BracketButton(
                            bracketOption = item,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(8.dp),
                            color = color
                        ) { option ->
                            uiEvent.onCalculatorButtonClicked(option)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.calculateShrunkFontSize(text: String): TextUnit {
    var shrunkFontSize = 36.sp
    val calculateIntrinsics: @Composable () -> ParagraphIntrinsics = {
        ParagraphIntrinsics(
            text = text,
            style = MaterialTheme.typography.displayMedium.copy(fontSize = shrunkFontSize),
            density = LocalDensity.current,
            fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
        )
    }

    var intrinsics = calculateIntrinsics()
    with(LocalDensity.current) {
        while (intrinsics.maxIntrinsicWidth > maxWidth.toPx()) {
            shrunkFontSize *= 0.9f
            intrinsics = calculateIntrinsics()
        }
    }

    return shrunkFontSize
}

@Preview
@Composable
fun CalculatorScreenPreview() {
    val uiState = CalculatorUiState.init.copy(
        input = "343265324",
        result = "4567"
    )
    CalculatorScreen(uiState = uiState, uiEvent = {
    })
}
