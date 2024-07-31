package com.mili.simplecalculator.screens

import com.mili.simplecalculator.domain.model.CalculatorInput

fun interface CalculatorUiEvent {
    fun onCalculatorButtonClicked(input: CalculatorInput)
}
