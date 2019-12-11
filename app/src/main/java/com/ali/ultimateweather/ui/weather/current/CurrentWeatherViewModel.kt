package com.ali.ultimateweather.ui.weather.current

import androidx.lifecycle.ViewModel
import com.ali.ultimateweather.data.reposiroty.UltimateWeatherRepository

class CurrentWeatherViewModel (
    private val ultimateWeatherRepository: UltimateWeatherRepository
): ViewModel() {
    val weather = ultimateWeatherRepository.getCurrentWeather("m")
}
