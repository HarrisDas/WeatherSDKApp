package com.example.weathersdk.internal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.domain.usecase.GetWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherViewModel @Inject constructor(
    private val useCase: GetWeatherForecastUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUIState(weatherForecast = null, cityName = ""))
    val uiState = _uiState.asStateFlow()

    fun onInteraction(interaction: WeatherViewModelInteraction) {
        when (interaction) {
            is WeatherViewModelInteraction.ScreenEntered -> fetchWeatherForecast(interaction.cityName)
        }
    }

    private fun fetchWeatherForecast(cityName: String?) {
        cityName?.let {

            viewModelScope.launch {
                _uiState.emit(WeatherUIState(cityName = it))
                when (val result = useCase.invoke(it)) {
                    is Result.Failure -> _uiState.emit(
                        WeatherUIState(
                            error = result.reason,
                            cityName = it
                        )
                    )

                    is Result.Success -> _uiState.emit(WeatherUIState(result.data, cityName = it))
                }
            }
        }
    }
}