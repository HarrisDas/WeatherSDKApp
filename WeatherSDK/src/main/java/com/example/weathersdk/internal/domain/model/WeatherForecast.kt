package com.example.weathersdk.internal.domain.model

internal data class WeatherForecast(val currentWeather: Forecast, val hourlyForeCast: List<Forecast>)