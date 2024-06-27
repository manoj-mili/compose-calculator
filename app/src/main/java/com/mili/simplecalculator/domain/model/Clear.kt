package com.mili.simplecalculator.domain.model

enum class Clear(val symbol: String) {
    AllClear("C"),
    Delete("Del"), // remove one char from last
}