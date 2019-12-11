package com.ali.ultimateweather.data.reposiroty

import androidx.lifecycle.LiveData
import com.ali.ultimateweather.data.db.CurrentWeatherDao
import com.ali.ultimateweather.data.db.entity.CurrentWeatherEntry
import com.ali.ultimateweather.data.network.WeatherNetworkDataSource
import com.ali.ultimateweather.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class UltimateWeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : UltimateWeatherRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever {
            newCurrentWeather -> persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<CurrentWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext currentWeatherDao.getWeatherEntry()
        }
    }

    private suspend fun initWeatherData() {

    }

    private suspend fun fetchCurrentWeather(){
        //TODO to be implemented
        weatherNetworkDataSource.fetchCurrentWeather("Beirut","m")
        // after fetch: response is persisted into the data because we are observing forever the downloaded current weather
        //
    }

    private fun isFetchedCurrentNeeded(lastFetchTime:ZonedDateTime):Boolean{
        val thirtyMinuteAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinuteAgo)
    }

    private fun persistFetchedCurrentWeather(fetchedWeather:CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

}