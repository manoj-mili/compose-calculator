package com.mili.simplecalculator.domain.model

enum class Operation(val operator: String) {
    Plus("+"),
    Minus("-"),
    Multiply("x"),
    Divide("/"),
    EqualTo("="),
    Percentage("%")
}
