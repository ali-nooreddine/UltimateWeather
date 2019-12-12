package com.ali.ultimateweather.ui.weather.current

import androidx.lifecycle.ViewModel
import com.ali.ultimateweather.data.reposiroty.UltimateWeatherRepository
import com.ali.ultimateweather.internal.lazyDeferred

class CurrentWeatherViewModel (
    weatherRepository: UltimateWeatherRepository
): ViewModel() {

    //TODO read metric from settings
    val weather by lazyDeferred {
        weatherRepository.getCurrentWeather("m")
    }

}
