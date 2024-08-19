package com.example.weathersdk.internal.data.repository

import com.example.weathersdk.internal.data.api.WeatherApiService
import com.example.weathersdk.internal.data.model.WeatherData
import com.example.weathersdk.internal.data.model.WeatherResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.data.model.Weather
import com.example.weathersdk.internal.data.model.WeatherForecastResponse
import com.example.weathersdk.internal.domain.model.Forecast
import io.mockk.coVerify
import java.time.LocalDateTime
import java.time.ZoneOffset

class WeatherForecastRepositoryImplTest {

    @MockK
    private lateinit var apiService: WeatherApiService

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private fun createSubject(apiKey: String? = null) =
        WeatherForecastRepositoryImpl(apiService, apiKey)

    @Test
    fun `getForecast should return success when API call is successful`() = runTest {
        // Given
        val weatherData = WeatherData(
            temp = 25.0,
            ts = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            weather = Weather(description = "Clear"),
            timezone = "Asia/Karachi"
        )
        val weatherResponse = WeatherResponse(listOf(weatherData))
        coEvery { apiService.getCurrentWeather(any(), any()) } returns Response.success(
            weatherResponse
        )

        // When
        val result = createSubject("key").getForecast("New York")

        // Then
        assertTrue(result is Result.Success)
        assertEquals(25.0, (result as Result.Success<Forecast>).data.temperature, 0.0)
        assertEquals("Clear", result.data.description)
    }

    @Test
    fun `getForecast should return failure when API call fails`() = runTest {
        // Given
        coEvery {
            apiService.getCurrentWeather(
                any(),
                any()
            )
        } returns mockk()

        // When
        val result = createSubject("key").getForecast("New York")

        // Then
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `getForecast should return failure when apiKey is not valid`() = runTest {
        val result = createSubject().getForecast("")
        coVerify(exactly = 0) {
            apiService.getCurrentWeather(any(), any())
        }

        assertTrue(result is Result.Failure)

    }

    @Test
    fun `getHourlyForecast should return success when API call is successful`() = runTest {
        // Given
        val weatherData = WeatherData(
            temp = 20.0,
            ts = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            weather = Weather(description = "Cloudy"),
            timezone = "Asia/Karachi"
        )
        val weatherResponse =
            WeatherForecastResponse(
                listOf(weatherData, weatherData.copy(temp = 22.0)),
                timezone = "Asia/Karachi"
            )
        coEvery { apiService.getHourlyForecast(any(), any(), any()) } returns Response.success(
            weatherResponse
        )

        // When
        val result = createSubject("key").getHourlyForecast("New York")

        // Then
        assertTrue(result is Result.Success)
        val forecasts = (result as Result.Success).data
        assertEquals(2, forecasts.size)
        assertEquals(20.0, forecasts[0].temperature, 0.0)
        assertEquals("Cloudy", forecasts[0].description)
        assertEquals(22.0, forecasts[1].temperature, 0.0)
    }

    @Test
    fun `getHourlyForecast should return failure when API call fails`() = runTest {
        // Given
        coEvery { apiService.getHourlyForecast(any(), any(), any()) } returns mockk()

        // When
        val result = createSubject("key").getHourlyForecast("New York")

        // Then
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `getHourlyForecast should return failure when apiKey is not valid`() = runTest {
        val result = createSubject().getHourlyForecast("")
        coVerify(exactly = 0) {
            apiService.getHourlyForecast(any(), any(), any())
        }

        assertTrue(result is Result.Failure)
    }
}