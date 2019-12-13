package com.ali.ultimateweather.ui.weather.current

import androidx.lifecycle.ViewModel
import com.ali.ultimateweather.data.reposiroty.UltimateWeatherRepository
import com.ali.ultimateweather.internal.UnitSystem
import com.ali.ultimateweather.internal.lazyDeferred

class CurrentWeatherViewModel (
    weatherRepository: UltimateWeatherRepository
): ViewModel() {

    private val unitSystem = UnitSystem.METRIC //get it from settings later

    //TODO get unit from settings
    val unit: String
        get() = unitSystem.name

    val weather by lazyDeferred {
        weatherRepository.getCurrentWeather(unit)
    }

}
