package com.mili.simplecalculator.domain.usecase

import com.mili.simplecalculator.domain.model.CalculatorInput.NumberInput
import com.mili.simplecalculator.domain.model.CalculatorInput.OperatorInput
import com.mili.simplecalculator.domain.model.Operation.EqualTo
import com.mili.simplecalculator.domain.model.Operation.Minus
import com.mili.simplecalculator.domain.model.Operation.Multiply
import com.mili.simplecalculator.domain.model.Operation.Plus
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CalculateUseCaseTest {
    private lateinit var SUT: CalculateUseCase

    @Before
    fun before() {
        SUT = CalculateUseCase()
    }

    @Test
    fun testInvoke() {
        val input = listOf(
            NumberInput(number = 10),
            OperatorInput(operation = Plus),
            NumberInput(number = 5),
            OperatorInput(operation = Multiply),
            NumberInput(number = 3),
            OperatorInput(operation = Plus),
            NumberInput(number = 10),
            OperatorInput(operation = Multiply),
            NumberInput(number = 5),
            OperatorInput(operation = Plus),
            NumberInput(number = 5),
            OperatorInput(operation = Minus),
            NumberInput(number = 2),
            OperatorInput(operation = EqualTo)
        )
        assert(SUT.invoke(input) == "78")
    }
}