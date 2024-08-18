package com.example.weathersdk.internal.domain.usecase

import com.example.weathersdk.internal.common.Result.*
import com.example.weathersdk.internal.domain.model.WeatherForecast
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.repository.WeatherForecastRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWeatherForecastUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: WeatherForecastRepository

    private lateinit var useCase: GetWeatherForecastUseCase

    @Before
    fun setUp() {
        useCase = GetWeatherForecastUseCase(repository)
    }

    @Test
    fun `invoke should return success when both forecast and hourlyForecast are successful`() = runBlocking {
        // Arrange
        val city = "TestCity"
        val forecast = Forecast(temperature = 25.0, timestamp = "10:00 AM", description = "Sunny")
        val hourlyForecast = listOf(forecast)

        coEvery { repository.getForecast(city) } returns Success(forecast)
        coEvery { repository.getHourlyForecast(city) } returns Success(hourlyForecast)

        // Act
        val result = useCase.invoke(city)

        // Assert
        assert(result is Success)
        assertEquals((result as Success).data, WeatherForecast(forecast, hourlyForecast))
        coVerify { repository.getForecast(city) }
        coVerify { repository.getHourlyForecast(city) }
    }

    @Test
    fun `invoke should return failure when forecast fails`() = runBlocking {
        // Arrange
        val city = "TestCity"
        val failureReason = "Network Error"

        coEvery { repository.getForecast(city) } returns Failure(failureReason)
        coEvery { repository.getHourlyForecast(city) } returns Success(emptyList())

        // Act
        val result = useCase.invoke(city)

        // Assert
        assert(result is Failure)
        assertEquals((result as Failure).reason, failureReason)
    }

    @Test
    fun `invoke should return failure when hourlyForecast fails`() = runBlocking {
        // Arrange
        val city = "TestCity"
        val forecast = Forecast(temperature = 25.0, timestamp = "10:00 AM", description = "Sunny")

        coEvery { repository.getForecast(city) } returns Success(forecast)
        coEvery { repository.getHourlyForecast(city) } returns Failure(Unit)

        // Act
        val result = useCase.invoke(city)

        // Assert
        assert(result is Failure)
        assertEquals((result as Failure).reason, "Unknown error")
        coVerify { repository.getForecast(city) }
        coVerify { repository.getHourlyForecast(city) }
    }

    @Test
    fun `invoke should handle both forecast and hourlyForecast failing`() = runBlocking {
        // Arrange
        val city = "TestCity"
        val forecastFailureReason = "Forecast Network Error"

        coEvery { repository.getForecast(city) } returns Failure(forecastFailureReason)
        coEvery { repository.getHourlyForecast(city) } returns Failure(Unit)

        // Act
        val result = useCase.invoke(city)

        // Assert
        assert(result is Failure)
        assertEquals((result as Failure).reason, forecastFailureReason)
        coVerify { repository.getForecast(city) }
        coVerify { repository.getHourlyForecast(city) }
    }
}
