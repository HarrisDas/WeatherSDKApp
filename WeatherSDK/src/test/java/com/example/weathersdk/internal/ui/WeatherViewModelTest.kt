package com.example.weathersdk.internal.ui

import app.cash.turbine.test
import com.example.weathersdk.TestCoroutineRule
import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.model.WeatherForecast
import com.example.weathersdk.internal.domain.usecase.GetWeatherForecastUseCase
import com.example.weathersdk.internal.ui.WeatherViewModelInteraction.ScreenEntered
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

internal class WeatherViewModelTest {

    @InjectMockKs
    lateinit var subject: WeatherViewModel

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var useCase: GetWeatherForecastUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coEvery {
            useCase.invoke("city")
        } returns Result.Success(
            data = WeatherForecast(
                currentWeather = Forecast(
                    22.0,
                    "Cloudy",
                    "11:00"
                ),
                hourlyForeCast = listOf(
                    Forecast(
                        23.0,
                        "Mostly Cloudy",
                        "12:00"
                    )
                )
            )
        )
    }

    @Test
    fun `fetch forecast when ScreenEntered interaction is received`() = runTest {
        subject.onInteraction(ScreenEntered)

        coVerify {
            useCase.invoke("city")
        }
    }


    @Test
    fun `emits ui state when forecast is fetched successfully`() = runTest {
        subject.onInteraction(ScreenEntered)

        subject.uiState.test {
            val item = awaitItem()
            assertNotNull(item.weatherForecast)

            val weatherForecast = item.weatherForecast!!
            assertEquals("Cloudy", weatherForecast.currentWeather.description)
            assertEquals(22.0, weatherForecast.currentWeather.temperature)
            assertEquals("11:00", weatherForecast.currentWeather.timestamp)

            assertEquals(1, weatherForecast.hourlyForeCast.size)

            val hourlyForecast = weatherForecast.hourlyForeCast.get(0)
            assertEquals("Mostly Cloudy", hourlyForecast.description)
            assertEquals(23.0, hourlyForecast.temperature)
            assertEquals("12:00", hourlyForecast.timestamp)

            assertNull(item.error)
        }

    }

    @Test
    fun `emits ui state when forecast is not fetched successfully`() = runTest {
        coEvery {
            useCase.invoke("city")
        } returns Result.Failure("Something went wrong")

        subject.onInteraction(ScreenEntered)

        subject.uiState.test {
            val item = awaitItem()

            assertNull(item.weatherForecast)
            assertEquals("Something went wrong", item.error)
        }

    }
}