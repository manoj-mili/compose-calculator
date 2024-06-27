package com.mili.simplecalculator.screens

import androidx.lifecycle.ViewModel
import com.mili.simplecalculator.domain.model.BracketType
import com.mili.simplecalculator.domain.model.CalculatorInput
import com.mili.simplecalculator.domain.model.Clear
import com.mili.simplecalculator.domain.model.Operation
import com.mili.simplecalculator.domain.usecase.CalculateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.IllegalStateException
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

class CalculatorViewModel(private val calculateUseCase: CalculateUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<CalculatorUiState> =
        MutableStateFlow(CalculatorUiState.init)

    val uiState: StateFlow<CalculatorUiState>
        get() = _uiState.asStateFlow()
    private val userCalculatorInputs: MutableList<CalculatorInput> = mutableListOf()

    private var currentBracketType: BracketType? = null

    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())


    fun onButtonClicked(input: CalculatorInput) {
        if (isInvalidInput(input)) return
        checkAndUpdateUserInputList(input)
        formatCalculatorInput(input)
    }

    private fun formatCalculatorInput(input: CalculatorInput) {
        when (input) {
            is CalculatorInput.OperatorInput -> input.handleOperatorInput()
            is CalculatorInput.BracketInput,
            is CalculatorInput.NumberInput,
            is CalculatorInput.SeparatorInput -> formatInput()

            is CalculatorInput.ClearInput -> input.handleClearOption()
        }
    }

    private fun isInvalidInput(input: CalculatorInput): Boolean {
        if (currentBracketType == BracketType.Open && (input as? CalculatorInput.OperatorInput)?.operation == (Operation.EqualTo)) {
            return true
        }
        val isInputNotBracket = (input is CalculatorInput.BracketInput).not()
        val lastInputOption = userCalculatorInputs.lastOrNull()
        val isLastInputBracket = (lastInputOption is CalculatorInput.BracketInput)
        return ((lastInputOption?.isOperatorInput() == true && input.isOperatorInput() && isInputNotBracket && isLastInputBracket)
                || (lastInputOption?.isSeparatorInput() == true && input.isSeparatorInput()))
    }

    private fun checkAndUpdateUserInputList(input: CalculatorInput) {
        when (input) {
            is CalculatorInput.ClearInput -> handleClearInput(input = input)
            is CalculatorInput.NumberInput -> handleNumberInput(input = input)
            is CalculatorInput.BracketInput -> handleBracketInput(input)
            else -> userCalculatorInputs.add(input)
        }
    }

    private fun handleBracketInput(input: CalculatorInput.BracketInput) {
        val lastInput = userCalculatorInputs.lastOrNull()
        when (lastInput) {
            is CalculatorInput.BracketInput,
            is CalculatorInput.ClearInput,
            is CalculatorInput.SeparatorInput -> return

            is CalculatorInput.NumberInput -> {
                if (currentBracketType == null) {
                    onButtonClicked(CalculatorInput.OperatorInput(operation = Operation.Multiply))
                }
            }

            is CalculatorInput.OperatorInput,
            null -> Unit
        }


        when (input.bracketType) {
            BracketType.Open,
            BracketType.Close -> throw IllegalStateException("Invalid state user cannot make these selections")

            BracketType.Ui -> {
                currentBracketType = when (currentBracketType) {
                    BracketType.Open -> {
                        userCalculatorInputs.add(input.copy(bracketType = BracketType.Close))
                        null
                    }

                    BracketType.Close -> {
                        userCalculatorInputs.add(input.copy(bracketType = BracketType.Open))
                        BracketType.Open
                    }

                    else -> {
                        userCalculatorInputs.add(input.copy(bracketType = BracketType.Open))
                        BracketType.Open
                    }
                }
            }
        }
    }

    private fun handleClearInput(input: CalculatorInput.ClearInput) {
        currentBracketType = when (input.clear) {
            Clear.AllClear -> {
                userCalculatorInputs.clear()
                null
            }

            Clear.Delete -> {
                val lastInput = userCalculatorInputs.removeLastOrNull()
                if (lastInput is CalculatorInput.NumberInput) {
                    updateLastNumberCorrectly(lastInput)
                    null
                } else {
                    when ((lastInput as? CalculatorInput.BracketInput)?.bracketType) {
                        BracketType.Open -> null
                        BracketType.Close -> BracketType.Open
                        BracketType.Ui -> throw IllegalStateException(" ${BracketType.Ui} should never be part of $userCalculatorInputs")
                        null -> null
                    }
                }
            }
        }
    }

    private fun updateLastNumberCorrectly(
        input: CalculatorInput.NumberInput,
    ) {
        val number = input.number
        val numberSplit = number.toString().split(".")
        val decimalPlaces = (numberSplit.getOrNull(1) ?: "").length
        val finalNumber = if (decimalPlaces > 0 && numberSplit[1] == "0") {
            val updatedNumber = numberSplit[0].dropLast(1)
            if (updatedNumber.isEmpty()) return
            else updatedNumber.toDouble()
        } else {
            val updatedNumber = numberSplit[1].dropLast(n=1)
            if (updatedNumber.isEmpty()) numberSplit[0].toDouble()
            else (numberSplit[0].plus(".").plus(updatedNumber)).toDouble()
        }
        val updatedLastIndexInput = input.copy(number = finalNumber)
        userCalculatorInputs.add(updatedLastIndexInput)
    }

    private fun handleNumberInput(input: CalculatorInput.NumberInput) {
        when (val lastInput = userCalculatorInputs.lastOrNull()) {
            is CalculatorInput.NumberInput -> appendToLastNumber(input, lastInput)
            is CalculatorInput.SeparatorInput -> appendAfterSeparator(input)
            is CalculatorInput.BracketInput -> {
                if (lastInput.bracketType == BracketType.Close) {
                    onButtonClicked(CalculatorInput.OperatorInput(operation = Operation.Multiply))
                }
                userCalculatorInputs.add(input)
            }

            else -> userCalculatorInputs.add(input)
        }
    }

    private fun appendToLastNumber(
        input: CalculatorInput.NumberInput,
        lastInput: CalculatorInput.NumberInput
    ) {
        var number = lastInput.number
        val numberSplit = number.toString().split(".")
        val decimalPlaces = (numberSplit.getOrNull(1) ?: "").length
        number = if (decimalPlaces > 0 && numberSplit[1] != "0") {
            number + input.number / 10.0.pow((decimalPlaces + 1).toDouble())
        } else {
            (number * 10) + input.number
        }
        val updatedLastIndexInput = lastInput.copy(number = number)
        userCalculatorInputs.removeLast()
        userCalculatorInputs.add(updatedLastIndexInput)
    }

    private fun appendAfterSeparator(input: CalculatorInput.NumberInput) {
        val secondLastInput =
            (userCalculatorInputs[userCalculatorInputs.size - 2] as CalculatorInput.NumberInput)
        var secondLastInputNumber = secondLastInput.number
        val updatedNumberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            maximumFractionDigits = 2
        }
        val decimalPlaces =
            (updatedNumberFormat.format(secondLastInputNumber).split(".").getOrNull(1) ?: "").length
        secondLastInputNumber += input.number / 10.0.pow((decimalPlaces + 1).toDouble())
        userCalculatorInputs.removeLast()
        userCalculatorInputs.removeLast()
        val updatedLastIndexInput = secondLastInput.copy(number = secondLastInputNumber)
        userCalculatorInputs.add(updatedLastIndexInput)
    }

    private fun CalculatorInput.ClearInput.handleClearOption() {
        var result = _uiState.value.result
        if (clear == Clear.AllClear || userCalculatorInputs.isEmpty()) {
            result = ""
        }
        formatInput(result)
    }

    private fun CalculatorInput.OperatorInput.handleOperatorInput() {
        when (operation) {
            Operation.Plus,
            Operation.Minus,
            Operation.Multiply,
            Operation.Divide,
            Operation.Percentage -> {
                operation.operator
            }

            Operation.EqualTo -> {
                calculateUseCase(userInputs = userCalculatorInputs)
            }
        }.also {
            var result = _uiState.value.result
            when {
                operation == Operation.EqualTo -> {
                    userCalculatorInputs.removeLast()
                    result = it
                }
            }
            formatInput(result)
        }
    }

    private fun formatInput(calculationResult: String? = null) {
        val formattedInput = StringBuilder("")
        userCalculatorInputs.forEach {
            when (it) {
                is CalculatorInput.ClearInput -> Unit
                is CalculatorInput.NumberInput -> {
                    formattedInput.append(numberFormat.format(it.number))
                }

                is CalculatorInput.OperatorInput -> {
                    val operator = when (it.operation) {
                        Operation.Plus,
                        Operation.Minus,
                        Operation.Multiply,
                        Operation.Divide,
                        Operation.Percentage -> {
                            it.operation.operator
                        }

                        Operation.EqualTo -> ""
                    }
                    formattedInput.append(operator)
                }

                is CalculatorInput.SeparatorInput -> {
                    formattedInput.append(it.separator.symbol)
                }

                is CalculatorInput.BracketInput -> {
                    formattedInput.append(it.bracketType.bracket)
                }
            }
        }
        _uiState.update {
            it.copy(
                input = formattedInput.toString(),
                result = calculationResult ?: it.result
            )
        }
    }
}
