package com.ali.ultimateweather.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ali.ultimateweather.data.reposiroty.UltimateWeatherRepository

/**
 * Created by Ali Noureddine on 12/12/2019.
 */
class CurrentWeatherViewModelFactory(
    private val weatherRepository: UltimateWeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository) as T
    }
}