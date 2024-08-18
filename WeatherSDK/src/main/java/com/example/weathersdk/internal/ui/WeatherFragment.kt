package com.example.weathersdk.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weathersdk.WeatherSDKEvent
import com.example.weathersdk.databinding.FragmentWeatherBinding
import com.example.weathersdk.internal.EventBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

const val KEY_CITY_NAME = "cityName"

@AndroidEntryPoint
internal class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() = _binding!!

    private val viewModel: WeatherViewModel by viewModels()
    val adapter = WeatherAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentWeatherBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRendering()
        initInteractions()
        attachViewModelEventListener()
    }

    private fun initRendering() {
        binding.rvWeather.adapter = adapter
    }

    private fun attachViewModelEventListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { weatherUIState ->
                    binding.renderWeatherForecast(weatherUIState)
                    weatherUIState.weatherForecast?.hourlyForeCast?.let {
                        adapter.submit(it)
                    }
                }


            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect {
                    if (it) activity?.onBackPressed()
                }
            }
        }
    }

    private fun initInteractions() {
        doOnSystemBackButtonClicked {
            viewLifecycleOwner.lifecycleScope.launch {
                EventBus.post(WeatherSDKEvent.OnFinished)
            }
        }

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun doOnSystemBackButtonClicked(action: () -> Unit) {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                action()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, callback
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.onInteraction(
            WeatherViewModelInteraction.ScreenEntered(
                requireArguments().getString(
                    KEY_CITY_NAME, ""
                )
            )
        )
    }

}