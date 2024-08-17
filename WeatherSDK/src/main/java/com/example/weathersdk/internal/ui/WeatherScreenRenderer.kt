package com.example.weathersdk.internal.ui

import com.example.weathersdk.R
import com.example.weathersdk.internal.domain.model.WeatherForecast

internal fun WeatherFragment.renderWeatherForecast(weatherForecast: WeatherForecast) {


//    binding.tvCityName
    with(binding) {
        tvTemperature.text = root.context.getString(
            R.string.temperature_label,
            weatherForecast.currentWeather.temperature.toString()
        )
        tvLocalTime.text = root.context.getString(
            R.string.local_time_label,
            weatherForecast.currentWeather.timestamp
        )
        tvTemperatureStatus.text = weatherForecast.currentWeather.description
    }
    adapter.submit(weatherForecast.hourlyForeCast)

}