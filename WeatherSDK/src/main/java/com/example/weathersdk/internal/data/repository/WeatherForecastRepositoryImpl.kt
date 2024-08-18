package com.example.weathersdk.internal.data.repository

import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.common.handleApiResponse
import com.example.weathersdk.internal.data.api.WeatherApiService
import com.example.weathersdk.internal.data.model.WeatherData
import com.example.weathersdk.internal.di.ApiKey
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.repository.WeatherForecastRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class WeatherForecastRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    @ApiKey private val apiKey: String?
) :
    WeatherForecastRepository {
    override suspend fun getForecast(city: String): Result<Forecast, String> {

        return apiKey?.let {
            val result = handleApiResponse {
                apiService.getCurrentWeather(
                    city = city,
                    apiKey = apiKey
                )
            }.map {
                it.data[0].mapWeather()
            }
            result
        } ?: run {
            Result.Failure("Api Key is not valid")
        }
    }

    private fun <T, R, E> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
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
        return apiKey?.let {

            handleApiResponse {

                apiService.getHourlyForecast(
                    city = city,
                    apiKey = apiKey
                )
            }.map {
                it.data.map { it.mapWeather() }
            }

        } ?: run {
            Result.Failure("Api Key is not valid")
        }
    }
}