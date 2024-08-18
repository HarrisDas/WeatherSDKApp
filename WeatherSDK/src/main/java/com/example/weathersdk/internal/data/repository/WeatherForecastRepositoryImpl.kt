package com.example.weathersdk.internal.data.repository

import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.common.handleApiResponse
import com.example.weathersdk.internal.data.api.WeatherApiService
import com.example.weathersdk.internal.data.model.WeatherData
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.repository.WeatherForecastRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class WeatherForecastRepositoryImpl @Inject constructor(private val apiService: WeatherApiService) :
    WeatherForecastRepository {
    override suspend fun getForecast(city: String): Result<Forecast, String> {

        val result = handleApiResponse {
            apiService.getCurrentWeather(
                city,
                "b1bc1ea940d84c05b42d8e6dccee56ad"
            )
        }.map {
            it.data[0].mapWeather()
        }
        return result
    }

    fun <T, R, E> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
        return when (this) {
            is Result.Success -> Result.Success(transform(data))
            is Result.Failure -> Result.Failure(this.reason)
        }
    }

    private fun WeatherData.mapWeather(): Forecast =
        Forecast(
            temp,
            description = weather.description,
            LocalDateTime.ofEpochSecond(ts, 0, ZoneOffset.UTC).toLocalTime().toString()
        )

    override suspend fun getHourlyForecast(city: String): Result<List<Forecast>, String> {
        return handleApiResponse {

            apiService.getHourlyForecast(city, hours = 24, "b1bc1ea940d84c05b42d8e6dccee56ad")
        }.map {
            it.data.map { it.mapWeather() }
        }
    }
}