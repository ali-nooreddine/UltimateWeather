package com.ali.ultimateweather.data.provider

import android.content.Context
import com.ali.ultimateweather.internal.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider {


    override fun getUnitProvider(): UnitSystem {
        var selectedName = preferences.getString("UNIT_SYSTEM", UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}