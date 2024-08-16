package com.example.weathersdk


sealed class WeatherSDKEvent {

    data object OnFinished : WeatherSDKEvent()
    data class OnFinishedWithError(val error: Throwable) : WeatherSDKEvent()
}