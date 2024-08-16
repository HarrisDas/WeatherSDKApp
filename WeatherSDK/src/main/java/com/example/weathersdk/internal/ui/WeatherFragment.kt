package com.example.weathersdk.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weathersdk.R
import com.example.weathersdk.WeatherSDKEvent
import com.example.weathersdk.internal.EventBus
import kotlinx.coroutines.launch

internal class WeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doOnSystemBackButtonClicked {
            viewLifecycleOwner.lifecycleScope.launch {
                EventBus.post(WeatherSDKEvent.OnFinished)
            }
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