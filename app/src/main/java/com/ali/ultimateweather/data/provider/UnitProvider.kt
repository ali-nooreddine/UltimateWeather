package com.ali.ultimateweather.data.provider

import com.ali.ultimateweather.internal.UnitSystem

/**
 * Created by Ali Noureddine on 12/13/2019.
 */
interface UnitProvider {
    fun getUnitProvider():UnitSystem
}