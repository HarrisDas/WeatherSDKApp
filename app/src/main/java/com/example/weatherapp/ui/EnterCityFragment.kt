package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentEnterCityBinding
import com.example.weathersdk.WeatherSDKManager

class EnterCityFragment : Fragment() {

    private var _binding: FragmentEnterCityBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEnterCityBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.button?.setOnClickListener {
            val sdk = WeatherSDKManager.getInstance()

            val fragment = sdk.createWeatherFragment("City")
            activity?.supportFragmentManager
                ?.beginTransaction()?.replace(R.id.container, fragment)
                ?.commit()
        }
    }
}