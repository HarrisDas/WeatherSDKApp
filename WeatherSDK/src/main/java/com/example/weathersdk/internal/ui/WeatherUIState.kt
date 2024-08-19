package com.example.weathersdk.internal.ui

import com.example.weathersdk.internal.domain.model.WeatherForecast

internal data class WeatherUIState(
    val weatherForecast: WeatherForecast? = null,
    val cityName: String
)