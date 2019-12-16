package com.ali.ultimateweather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ali.ultimateweather.data.db.entity.WEATHER_LOCATION_ID
import com.ali.ultimateweather.data.db.entity.WeatherLocation

/**
 * Created by Ali Noureddine on 12/16/2019.
 */
@Dao
interface WeatherLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherLocation: WeatherLocation)

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<WeatherLocation>
}