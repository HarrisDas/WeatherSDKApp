package com.example.weathersdk.internal.ui

import android.content.Context
import com.example.weathersdk.R
import com.example.weathersdk.databinding.FragmentWeatherBinding
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.model.WeatherForecast
import com.example.weathersdk.mockFields
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

internal class WeatherScreenRendererKtTest {

    @MockK
    lateinit var binding: FragmentWeatherBinding

    @MockK
    lateinit var context: Context

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        binding.mockFields(relaxed = true)
        every { binding.root.context } returns context
    }

    @Test
    fun `test renderWeatherForecast sets correct values`() {
        // Arrange
        val weatherForecast = WeatherForecast(
            currentWeather = Forecast(
                temperature = 25.0,
                timestamp = "10:00",
                description = "Sunny"
            ),
            hourlyForeCast = listOf(/* mock hourly forecasts */)
        )

        every { context.getString(R.string.label_city, "Berlin") } returns "Berlin"
        every { context.getString(R.string.temperature_label, "25.0") } returns "25.0"
        every { context.getString(R.string.local_time_label, "10:00") } returns "10:00"

        // Act
        binding.renderWeatherForecast(
            WeatherUIState(
                weatherForecast = weatherForecast,
                cityName = "Berlin"
            )
        )

        // Assert
        verify { binding.tvCityName.text = "Berlin" }
        verify { binding.tvTemperature.text = "25.0" }
        verify { binding.tvLocalTime.text = "10:00" }
        verify { binding.tvTemperatureStatus.text = "Sunny" }
    }
}