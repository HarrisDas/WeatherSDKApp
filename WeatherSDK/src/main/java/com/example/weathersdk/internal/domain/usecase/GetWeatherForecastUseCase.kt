package com.example.weathersdk.internal.domain.usecase

import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.domain.model.Forecast
import com.example.weathersdk.internal.domain.model.WeatherForecast
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor() {
    suspend fun invoke(): Result<WeatherForecast, String> {
        TODO()
    }


}