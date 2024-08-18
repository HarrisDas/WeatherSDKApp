package com.example.weathersdk.internal.data.api

import com.example.weathersdk.internal.data.model.WeatherForecastResponse
import com.example.weathersdk.internal.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("current/")
    suspend fun getCurrentWeather(
        @Query("city") city: String,
        @Query("key") apiKey: String
    ): Response<WeatherResponse>

    @GET("forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("city") city: String,
        @Query("hours") hours: Int = 24,
        @Query("key") apiKey: String
    ): Response<WeatherForecastResponse>
}