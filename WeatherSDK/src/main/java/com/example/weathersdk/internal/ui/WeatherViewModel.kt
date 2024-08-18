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

    private val _uiState = MutableStateFlow(WeatherUIState(weatherForecast = null))
    val uiState = _uiState.asStateFlow()

    fun onInteraction(interaction: WeatherViewModelInteraction) {
        when (interaction) {
            WeatherViewModelInteraction.ScreenEntered -> fetchWeatherForecast()
        }
    }

    private fun fetchWeatherForecast() {
        viewModelScope.launch {
            when (val result = useCase.invoke("")) {
                is Result.Failure -> _uiState.emit(WeatherUIState(error = result.reason))
                is Result.Success -> _uiState.emit(WeatherUIState(result.data))
            }
        }
    }
}