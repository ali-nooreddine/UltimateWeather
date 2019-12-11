package com.ali.ultimateweather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ali.ultimateweather.data.db.entity.CURRENT_WEATHER_ID
import com.ali.ultimateweather.data.db.entity.CurrentWeatherEntry

/**
 * Created by Ali Noureddine on 12/11/2019.
 */
@Dao
interface CurrentWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherEntry():LiveData<CurrentWeatherEntry>
}