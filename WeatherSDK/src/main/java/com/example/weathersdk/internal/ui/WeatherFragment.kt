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
import kotlinx.coroutines.launch

internal class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    val binding: FragmentWeatherBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentWeatherBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            viewLifecycleOwner,
            callback
        )
    }

}