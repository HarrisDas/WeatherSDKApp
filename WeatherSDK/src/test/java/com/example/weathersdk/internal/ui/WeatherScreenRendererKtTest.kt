package com.example.weathersdk.internal.ui

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.weathersdk.R
import com.example.weathersdk.databinding.FragmentWeatherBinding
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.model.WeatherForecast
import com.example.weathersdk.mockFields
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class WeatherScreenRendererKtTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var fragment: WeatherFragment

    @MockK
    lateinit var binding: FragmentWeatherBinding

    @MockK
    lateinit var adapter: WeatherAdapter

    @MockK
    lateinit var context: Context

    @Before
    fun setUp() {
        mockkStatic(ContextCompat::class)

        every { fragment.binding } returns binding
        binding.mockFields(relaxed = true)
        every { binding.root.context } returns context
        every { fragment.adapter } returns adapter
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

        every { context.getString(R.string.temperature_label, "25.0") } returns "25.0"
        every { context.getString(R.string.local_time_label, "10:00") } returns "10:00"
        every { adapter.submit(any()) } returns Unit

        // Act
        fragment.renderWeatherForecast(weatherForecast)

        // Assert
        verify { binding.tvTemperature.text = "25.0" }
        verify { binding.tvLocalTime.text = "10:00" }
        verify { binding.tvTemperatureStatus.text = "Sunny" }
        verify { adapter.submit(weatherForecast.hourlyForeCast) }
    }
}