package com.mili.simplecalculator.domain.usecase

import com.mili.simplecalculator.domain.model.BracketType
import com.mili.simplecalculator.domain.model.CalculatorInput
import com.mili.simplecalculator.domain.model.Operation
import java.lang.IllegalStateException
import java.text.NumberFormat
import java.util.Locale

class CalculateUseCase {
    /**
     * this follows BODMAS to solve the input
     */
    operator fun invoke(userInputs: List<CalculatorInput>): String {
        val mutableUserInputs = userInputs.toMutableList()
        val bracketsUpdatedInputs = performBracketOperation(mutableUserInputs)
        val percentageUpdatedInputs = performPercentageOperation(bracketsUpdatedInputs)
        val multiplicationUpdatedInputs = performMultiplication(percentageUpdatedInputs)
        val divisionUpdatedInputs = performDivision(multiplicationUpdatedInputs)
        val plusUpdatedInputs = performAddition(divisionUpdatedInputs)
        val minusUpdatedInputs = performSubtraction(plusUpdatedInputs)

        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            maximumFractionDigits = 2
        }

        return numberFormat.format((minusUpdatedInputs[0] as CalculatorInput.NumberInput).number)
    }

    private fun performBracketOperation(inputs: List<CalculatorInput>): List<CalculatorInput> {
        val bracketsResolvedInputs = mutableListOf<CalculatorInput>()

        var i = 0
        var openBracketIndex = -1
        var closeBracketIndex = -1
        while (i < inputs.size) {
            val input = inputs[i]
            if (input is CalculatorInput.BracketInput) {
                when (input.bracketType) {
                    BracketType.Open -> openBracketIndex = i
                    BracketType.Close -> closeBracketIndex = i
                    BracketType.Ui -> throw IllegalStateException("Invalid this is only expected and used for Ui")
                }

                if (openBracketIndex != -1 && closeBracketIndex != -1 && closeBracketIndex > openBracketIndex) {
                    // process the input to solve the inputs in the bracket
                    val bracketInputs = inputs.subList(openBracketIndex + 1, closeBracketIndex)
                    val multiplicationUpdatedInputs = performMultiplication(bracketInputs)
                    val divisionUpdatedInputs = performDivision(multiplicationUpdatedInputs)
                    val plusUpdatedInputs = performAddition(divisionUpdatedInputs)
                    val minusUpdatedInputs = performSubtraction(plusUpdatedInputs)
                    bracketsResolvedInputs.add(minusUpdatedInputs[0])
                    openBracketIndex = -1
                    closeBracketIndex = -1
                }
            } else {
                // if open bracket is found then no need to add and wait until we find the close bracket and process it
                // so check the default state of -1 to continue adding
                if (openBracketIndex == -1) {
                    bracketsResolvedInputs.add(input)
                }
            }
            i++
        }
        return bracketsResolvedInputs
    }

    private fun performPercentageOperation(inputs: List<CalculatorInput>): List<CalculatorInput> {
        val percentageUpdatedInputs = mutableListOf<CalculatorInput>()
        var i = 0
        while (i < inputs.size) {
            val input = inputs[i]
            if (input is CalculatorInput.OperatorInput && input.operation == Operation.Percentage) {
                val lastInputNumber = (percentageUpdatedInputs.lastOrNull() as? CalculatorInput.NumberInput)?.number
                val previousNumber = lastInputNumber ?: (inputs[i - 1] as CalculatorInput.NumberInput).number
                val percentageResult = previousNumber / 100
                percentageUpdatedInputs.removeLast()
                percentageUpdatedInputs.add(CalculatorInput.NumberInput(percentageResult))
            } else {
                percentageUpdatedInputs.add(input)
            }
            i++
        }
        return percentageUpdatedInputs
    }

    private fun performMultiplication(inputs: List<CalculatorInput>): List<CalculatorInput> {
        val multiplicationUpdatedInputs = mutableListOf<CalculatorInput>()
        var i = 0
        while (i < inputs.size) {
            val input = inputs[i]
            if (input is CalculatorInput.OperatorInput && input.operation == Operation.Multiply) {
                val lastInputNumber =
                    (multiplicationUpdatedInputs.lastOrNull() as? CalculatorInput.NumberInput)?.number
                val previousNumber =
                    lastInputNumber ?: (inputs[i - 1] as CalculatorInput.NumberInput).number
                val nextNumber = (inputs[i + 1] as CalculatorInput.NumberInput).number
                val multiplyResult = previousNumber * nextNumber
                multiplicationUpdatedInputs.removeLast()
                multiplicationUpdatedInputs.add(CalculatorInput.NumberInput(multiplyResult))
                i++ // incrementing as we already consumed the next number
            } else {
                multiplicationUpdatedInputs.add(input)
            }
            i++
        }
        return multiplicationUpdatedInputs
    }

    private fun performDivision(inputs: List<CalculatorInput>): List<CalculatorInput> {
        val divisionUpdatedInputs = mutableListOf<CalculatorInput>()
        var i = 0
        while (i < inputs.size) {
            val input = inputs[i]
            if (input is CalculatorInput.OperatorInput && input.operation == Operation.Divide) {
                val lastInputNumber =
                    (divisionUpdatedInputs.lastOrNull() as? CalculatorInput.NumberInput)?.number
                val previousNumber =
                    lastInputNumber ?: (inputs[i - 1] as CalculatorInput.NumberInput).number
                val nextNumber = (inputs[i + 1] as CalculatorInput.NumberInput).number
                val divisionResult = previousNumber / nextNumber
                divisionUpdatedInputs.removeLast()
                divisionUpdatedInputs.add(CalculatorInput.NumberInput(divisionResult))
                i++ // incrementing as we already consumed the next number
            } else {
                divisionUpdatedInputs.add(input)
            }
            i++
        }
        return divisionUpdatedInputs
    }

    private fun performAddition(inputs: List<CalculatorInput>): List<CalculatorInput> {
        val plusUpdatedInputs = mutableListOf<CalculatorInput>()
        var i = 0
        while (i < inputs.size) {
            val input = inputs[i]
            if (input is CalculatorInput.OperatorInput && input.operation == Operation.Plus) {
                val lastInputNumber =
                    (plusUpdatedInputs.lastOrNull() as? CalculatorInput.NumberInput)?.number
                val previousNumber =
                    lastInputNumber ?: (inputs[i - 1] as CalculatorInput.NumberInput).number
                val nextNumber = (inputs[i + 1] as CalculatorInput.NumberInput).number
                val plusResult = previousNumber + nextNumber
                plusUpdatedInputs.removeLastOrNull()
                plusUpdatedInputs.add(CalculatorInput.NumberInput(plusResult))
                i++ // incrementing as we already consumed the next number
            } else {
                plusUpdatedInputs.add(input)
            }
            i++
        }
        return plusUpdatedInputs
    }

    private fun performSubtraction(plusUpdatedInputs: List<CalculatorInput>): List<CalculatorInput> {
        val minusUpdatedInputs = mutableListOf<CalculatorInput>()
        var i = 0
        while (i < plusUpdatedInputs.size) {
            val input = plusUpdatedInputs[i]
            if (input is CalculatorInput.OperatorInput && input.operation == Operation.Minus) {
                val previousNumber =
                    (plusUpdatedInputs[i - 1] as CalculatorInput.NumberInput).number
                val nextNumber = (plusUpdatedInputs[i + 1] as CalculatorInput.NumberInput).number
                val minusResult = previousNumber - nextNumber
                minusUpdatedInputs.removeLast()
                minusUpdatedInputs.add(CalculatorInput.NumberInput(minusResult))
                i++ // incrementing as we already consumed the next number
            } else {
                minusUpdatedInputs.add(input)
            }
            i++
        }
        return minusUpdatedInputs
    }
}
