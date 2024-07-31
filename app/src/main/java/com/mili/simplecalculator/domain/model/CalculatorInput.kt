package com.mili.simplecalculator.domain.model

sealed interface CalculatorInput {

    data class NumberInput(val number: Double) : CalculatorInput
    data class OperatorInput(val operation: Operation) : CalculatorInput
    data class SeparatorInput(val separator: Separator) : CalculatorInput
    data class ClearInput(val clear: Clear) : CalculatorInput
    data class BracketInput(val bracketType: BracketType) : CalculatorInput

    fun isOperatorInput(): Boolean = this is OperatorInput
    fun isSeparatorInput(): Boolean = this is SeparatorInput
}
