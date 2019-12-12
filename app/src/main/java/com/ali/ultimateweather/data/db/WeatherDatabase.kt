package com.ali.ultimateweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ali.ultimateweather.data.db.entity.CurrentWeatherEntry
import com.ali.ultimateweather.data.db.entity.PersistentConverter

/**
 * Created by Ali Noureddine on 12/11/2019.
 */
@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1
)
@TypeConverters(PersistentConverter::class)
abstract class WeatherDatabase:RoomDatabase() {

    abstract fun currentWeatherDao():CurrentWeatherDao

    companion object {
        @Volatile private var instance:WeatherDatabase?=null
        // to make sure no 2 threads are doing the same things
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDatabase::class.java, "weather.db"
            )
                .build()
    }
}
