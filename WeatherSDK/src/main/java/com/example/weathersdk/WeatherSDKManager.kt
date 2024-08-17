package com.example.weathersdk

import androidx.fragment.app.Fragment
import com.example.weathersdk.internal.ApiKeyManager
import com.example.weathersdk.internal.WeatherSDKManagerImpl
import kotlinx.coroutines.flow.Flow

interface WeatherSDKManager {

    fun createWeatherFragment(cityName: String): Fragment
    fun observeWeatherSdkEvents(): Flow<WeatherSDKEvent>

    companion object {
        @Volatile
        private var instance: WeatherSDKManager? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: WeatherSDKManagerImpl().also { instance = it }
        }

        fun initializeSDK(apiKey: String) {
            ApiKeyManager.getInstance().setApiKey(apiKey)
        }
    }
}
