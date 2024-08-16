package com.example.weathersdk.internal

import com.example.weathersdk.WeatherSDKEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal object EventBus {
    private val _events = MutableSharedFlow<WeatherSDKEvent>()
    val events: SharedFlow<WeatherSDKEvent> = _events.asSharedFlow()

    suspend fun post(event: WeatherSDKEvent) {
        _events.emit(event)
    }
}