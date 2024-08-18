package com.example.weathersdk.internal.ui

import com.example.weathersdk.R
import com.example.weathersdk.databinding.FragmentWeatherBinding

internal fun FragmentWeatherBinding.renderWeatherForecast(
    weatherUIState: WeatherUIState
) = with(weatherUIState) {
    tvCityName.text = root.context.getString(
        R.string.label_city,
        cityName
    )

    weatherForecast?.let {
        tvTemperature.text = root.context.getString(
            R.string.temperature_label,
            it.currentWeather.temperature.toString()
        )

        tvLocalTime.text = root.context.getString(
            R.string.local_time_label,
            it.currentWeather.timestamp
        )

        tvTemperatureStatus.text = it.currentWeather.description
    }
}