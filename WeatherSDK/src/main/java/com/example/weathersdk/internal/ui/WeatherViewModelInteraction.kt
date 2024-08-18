package com.example.weathersdk.internal.ui

sealed interface WeatherViewModelInteraction {
    data class ScreenEntered(val cityName: String) : WeatherViewModelInteraction

}
