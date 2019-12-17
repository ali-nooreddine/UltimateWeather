package com.ali.ultimateweather.data.reposiroty

import androidx.lifecycle.LiveData
import com.ali.ultimateweather.data.db.CurrentWeatherDao
import com.ali.ultimateweather.data.db.WeatherLocationDao
import com.ali.ultimateweather.data.db.entity.CurrentWeatherEntry
import com.ali.ultimateweather.data.db.entity.WeatherLocation
import com.ali.ultimateweather.data.network.WeatherNetworkDataSource
import com.ali.ultimateweather.data.network.response.CurrentWeatherResponse
import com.ali.ultimateweather.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class UltimateWeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val weatherLocationDao: WeatherLocationDao,
    private val locationProvider: LocationProvider
) : UltimateWeatherRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever {
            newCurrentWeather -> persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(unit: String): LiveData<CurrentWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData(unit)
            return@withContext currentWeatherDao.getWeatherEntry()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private suspend fun initWeatherData(unit: String) {
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather(unit)
            return
        }

        if (shouldFetch(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather(unit)

    }

    private suspend fun fetchCurrentWeather(unit: String){
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            unit
        )
        // after fetch: response is persisted into the data because we are observing forever the downloaded current weather
    }

    private fun shouldFetch(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinuteAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinuteAgo)
    }

    private fun persistFetchedCurrentWeather(fetchedWeather:CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            val location = fetchedWeather.location
            location.id = 0
            val id = weatherLocationDao.upsert(location)
            print(id)
        }
    }

}