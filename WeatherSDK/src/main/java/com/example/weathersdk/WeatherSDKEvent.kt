package com.example.weathersdk

/**
 * Represents events emitted by the Weather SDK.
 *
 * The [WeatherSDKEvent] sealed class defines the different types of events that can be
 * emitted by the Weather SDK. These events can be used to monitor the terminal events that fragment has been closed,
 * including errors.
 *
 * There are two types of events:
 * - [OnFinished]: Indicates that an fragment has been closed.
 * - [OnFinishedWithError]: Indicates that an fragment has been closed with an error.
 *
 * @sealed
 * @author Harris Das
 */
sealed class WeatherSDKEvent {

    /**
     * Represents that fragment has been closed.
     */
    data object OnFinished : WeatherSDKEvent()

    /**
     * Represents that fragment has been closed with an error.
     *
     * @param error The [Throwable] representing the error that occurred during the operation.
     */
    data class OnFinishedWithError(val error: Throwable) : WeatherSDKEvent()
}