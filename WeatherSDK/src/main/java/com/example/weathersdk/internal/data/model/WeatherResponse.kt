package com.example.weathersdk.internal.data.model

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("data")
    val data: List<WeatherData>,

    @SerializedName("timezone")
    val timezone: String,
)

data class WeatherResponse(
    @SerializedName("data")
    val data: List<WeatherData>
)

data class WeatherData(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("ts")
    val ts: Long,
    @SerializedName("weather")
    val weather: Weather,
)

data class Weather(
    @SerializedName("description")
    val description: String,
)