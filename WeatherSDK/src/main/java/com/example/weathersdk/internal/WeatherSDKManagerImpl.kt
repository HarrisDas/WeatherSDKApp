package com.example.weathersdk.internal

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.weathersdk.WeatherSDKEvent
import com.example.weathersdk.WeatherSDKManager
import com.example.weathersdk.internal.ui.WeatherFragment
import kotlinx.coroutines.flow.Flow

internal class WeatherSDKManagerImpl : WeatherSDKManager {
    override fun createWeatherFragment(cityName: String): Fragment {
        val fragment = WeatherFragment()
        val args = Bundle().apply {
            putString("cityName", cityName)
        }
        fragment.arguments = args
        return fragment
    }

    override fun observeWeatherSdkEvents(): Flow<WeatherSDKEvent> {
        return EventBus.events
    }
}
