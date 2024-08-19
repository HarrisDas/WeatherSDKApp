package com.example.weathersdk.internal.data.repository

import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.common.handleApiResponse
import com.example.weathersdk.internal.data.api.WeatherApiService
import com.example.weathersdk.internal.data.model.WeatherData
import com.example.weathersdk.internal.di.ApiKey
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.repository.WeatherForecastRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
                it.data[0].mapWeather(it.data[0].timezone!!)
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

    private fun WeatherData.mapWeather(tz: String): Forecast =
        Forecast(
            temp,
            description = weather.description,
            convertTimestampToLocalTime(ts, tz)
        )

    private fun convertTimestampToLocalTime(timestamp: Long, zoneId: String?): String {
        // Create an Instant from the timestamp
        val instant = Instant.ofEpochSecond(timestamp)

        val zone = zoneId?.let {
            ZoneId.of(zoneId)
        } ?: ZoneId.systemDefault()
        // Convert the Instant to a ZonedDateTime in the system's default time zone
        val zonedDateTime = ZonedDateTime.ofInstant(instant, zone)

        // Format the ZonedDateTime to a string
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return zonedDateTime.format(formatter)
    }

    override suspend fun getHourlyForecast(city: String): Result<List<Forecast>, String> {
        return apiKey?.let {

            handleApiResponse {

                apiService.getHourlyForecast(
                    city = city,
                    apiKey = apiKey
                )
            }.map {response->

                response.data.map { it.mapWeather(response.timezone) }
            }

        } ?: run {
            Result.Failure("Api Key is not valid")
        }
    }
}