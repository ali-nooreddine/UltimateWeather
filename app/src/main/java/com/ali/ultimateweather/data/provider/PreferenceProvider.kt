package com.ali.ultimateweather.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Created by Ali Noureddine on 12/17/2019.
 */
abstract class PreferenceProvider(context: Context) {
    // safe usage of context to use application context: if context is gone then we still have refrence for Application Context
    // then is the application context is gone then the below code will never run
    private val appContext = context.applicationContext

    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

}