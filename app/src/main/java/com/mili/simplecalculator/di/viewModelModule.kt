package com.mili.simplecalculator.di

import com.mili.simplecalculator.domain.usecase.CalculateUseCase
import com.mili.simplecalculator.screens.CalculatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    factory {
        CalculateUseCase()
    }

    viewModel {
        CalculatorViewModel(calculateUseCase = get())
    }
}