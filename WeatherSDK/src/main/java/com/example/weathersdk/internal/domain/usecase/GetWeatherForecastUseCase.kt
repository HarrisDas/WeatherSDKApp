package com.example.weathersdk.internal.domain.usecase

import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.domain.model.WeatherForecast
import com.example.weathersdk.internal.domain.repository.WeatherForecastRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class GetWeatherForecastUseCase @Inject constructor(private val repository: WeatherForecastRepository) {
    suspend fun invoke(city: String): Result<WeatherForecast, String> = coroutineScope {
        val forecastDeferred = async { repository.getForecast(city) }
        val hourlyForecastDeferred = async { repository.getHourlyForecast(city) }

        val forecast = forecastDeferred.await()
        val hourlyForecast = hourlyForecastDeferred.await()

        if (forecast is Result.Success && hourlyForecast is Result.Success) {
            Result.Success(WeatherForecast(forecast.data, hourlyForecast.data))
        } else {
            Result.Failure((forecast as? Result.Failure)?.reason ?: "Unknown error")
        }
    }
}