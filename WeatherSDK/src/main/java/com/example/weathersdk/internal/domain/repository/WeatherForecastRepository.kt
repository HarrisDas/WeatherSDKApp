package com.example.weathersdk.internal.domain.repository

import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.domain.model.Forecast

interface WeatherForecastRepository {
    suspend fun getForecast(city: String): Result<Forecast, String>
    suspend fun getHourlyForecast(city: String): Result<List<Forecast>, String>
}