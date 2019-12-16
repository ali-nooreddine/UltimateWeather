package com.ali.ultimateweather.data.reposiroty

import androidx.lifecycle.LiveData
import com.ali.ultimateweather.data.db.entity.CurrentWeatherEntry
import com.ali.ultimateweather.data.db.entity.WeatherLocation

/**
 * Created by Ali Noureddine on 12/11/2019.
 */
interface UltimateWeatherRepository {
    suspend fun getCurrentWeather(units: String): LiveData<CurrentWeatherEntry>

    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}