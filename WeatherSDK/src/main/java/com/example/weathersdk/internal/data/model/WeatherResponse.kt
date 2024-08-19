package com.example.weathersdk.internal.data.model

import com.google.gson.annotations.SerializedName

internal data class WeatherForecastResponse(
    @SerializedName("data")
    val data: List<WeatherData>,

    @SerializedName("timezone")
    val timezone: String,
)

internal data class WeatherResponse(
    @SerializedName("data")
    val data: List<WeatherData>
)

internal data class WeatherData(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("ts")
    val ts: Long,
    @SerializedName("weather")
    val weather: Weather,
)

internal data class Weather(
    @SerializedName("description")
    val description: String,
)