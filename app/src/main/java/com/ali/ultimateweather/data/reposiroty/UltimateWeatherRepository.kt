package com.ali.ultimateweather.data.reposiroty

import androidx.lifecycle.LiveData
import com.ali.ultimateweather.data.db.entity.CurrentWeatherEntry

/**
 * Created by Ali Noureddine on 12/11/2019.
 */
interface UltimateWeatherRepository {
    suspend fun getCurrentWeather(metric:Boolean): LiveData<CurrentWeatherEntry>
}