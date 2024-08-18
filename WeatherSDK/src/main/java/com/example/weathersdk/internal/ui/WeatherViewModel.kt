package com.example.weathersdk.internal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersdk.WeatherSDKEvent
import com.example.weathersdk.internal.EventBus
import com.example.weathersdk.internal.common.Result
import com.example.weathersdk.internal.domain.usecase.GetWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherViewModel @Inject constructor(
    private val useCase: GetWeatherForecastUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUIState(weatherForecast = null, cityName = ""))
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<Boolean>()
    val event = eventChannel.receiveAsFlow()

    fun onInteraction(interaction: WeatherViewModelInteraction) {
        when (interaction) {
            is WeatherViewModelInteraction.ScreenEntered -> fetchWeatherForecast(interaction.cityName)
        }
    }

    private fun fetchWeatherForecast(cityName: String) {
        viewModelScope.launch {

            _uiState.emit(WeatherUIState(cityName = cityName))
            when (val result = useCase.invoke(cityName)) {
                is Result.Failure -> {
                    EventBus.post(WeatherSDKEvent.OnFinishedWithError(Throwable(result.reason)))
                    eventChannel.send(true)
                }

                is Result.Success -> _uiState.emit(WeatherUIState(result.data, cityName = cityName))
            }
        }
    }
}