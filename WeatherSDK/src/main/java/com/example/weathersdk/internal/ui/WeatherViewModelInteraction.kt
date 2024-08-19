package com.example.weathersdk.internal.ui

internal sealed interface WeatherViewModelInteraction {
    data class ScreenEntered(val cityName: String) : WeatherViewModelInteraction

}
