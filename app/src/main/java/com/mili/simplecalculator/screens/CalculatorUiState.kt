package com.mili.simplecalculator.screens

import com.mili.simplecalculator.domain.model.BracketType
import com.mili.simplecalculator.domain.model.Operation
import com.mili.simplecalculator.domain.model.CalculatorInput
import com.mili.simplecalculator.domain.model.Clear
import com.mili.simplecalculator.domain.model.Separator

data class CalculatorUiState(
    val input:String,
    val result: String,
    val buttons: List<CalculatorInput>
) {
    companion object {
        private val row1 = listOf(
            CalculatorInput.NumberInput(1.0),
            CalculatorInput.NumberInput(2.0),
            CalculatorInput.NumberInput(3.0),
            CalculatorInput.OperatorInput(Operation.Plus),
        )

        private val row2 = listOf(
            CalculatorInput.NumberInput(4.0),
            CalculatorInput.NumberInput(5.0),
            CalculatorInput.NumberInput(6.0),
            CalculatorInput.OperatorInput(Operation.Minus),
        )

        private val row3 = listOf(
            CalculatorInput.NumberInput(7.0),
            CalculatorInput.NumberInput(8.0),
            CalculatorInput.NumberInput(9.0),
            CalculatorInput.OperatorInput(Operation.Multiply),
        )
        private val row4 = listOf(
            CalculatorInput.NumberInput(0.0),
            CalculatorInput.SeparatorInput(Separator.Dot),
            CalculatorInput.ClearInput(Clear.Delete),
            CalculatorInput.OperatorInput(Operation.EqualTo),
        )
        private val row5 = listOf(
            CalculatorInput.ClearInput(Clear.AllClear),
            CalculatorInput.BracketInput(BracketType.Ui),
            CalculatorInput.OperatorInput(Operation.Percentage),
            CalculatorInput.OperatorInput(Operation.Divide),
        )
        val init: CalculatorUiState = CalculatorUiState(
            input = "",
            result = "",
            buttons = row5 + row3+ row2 + row1 + row4
        )
    }
}
