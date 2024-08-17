package com.example.weathersdk.internal.ui

sealed interface WeatherViewModelInteraction {
    data object ScreenEntered : WeatherViewModelInteraction

}
