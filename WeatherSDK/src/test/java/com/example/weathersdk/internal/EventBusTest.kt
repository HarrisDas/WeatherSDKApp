package com.example.weathersdk.internal

import app.cash.turbine.test
import com.example.weathersdk.WeatherSDKEvent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions

internal class EventBusTest {

    @Test
    fun `emits new value when post is called`() = runTest {
        EventBus.events.test {
            EventBus.post(WeatherSDKEvent.OnFinished)

            Assertions.assertEquals(WeatherSDKEvent.OnFinished, awaitItem())
        }
    }
}