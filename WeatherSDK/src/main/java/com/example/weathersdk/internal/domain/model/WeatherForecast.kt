package com.example.weathersdk.internal.domain.model

data class WeatherForecast(val currentWeather: Forecast, val hourlyForeCast: List<Forecast>)