package com.mili.simplecalculator

import android.app.Application
import com.mili.simplecalculator.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CalculatorApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CalculatorApp)
            modules(viewModelModule)
        }
    }
}