package com.ali.ultimateweather.ui.weather.current

import androidx.lifecycle.ViewModel
import com.ali.ultimateweather.data.provider.UnitProvider
import com.ali.ultimateweather.data.reposiroty.UltimateWeatherRepository
import com.ali.ultimateweather.internal.lazyDeferred

class CurrentWeatherViewModel (
    private val weatherRepository: UltimateWeatherRepository,
    unitProvider: UnitProvider
): ViewModel() {

    private val unitSystem = unitProvider.getUnitProvider()

    val selectedUnit: String
        get() = unitSystem.code

    val weather by lazyDeferred {
        weatherRepository.getCurrentWeather(selectedUnit)
    }

    val weatherLocation by lazyDeferred {
        weatherRepository.getWeatherLocation()
    }
}
