package com.example.weatherapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentEnterCityBinding
import com.example.weathersdk.WeatherSDKManager
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterCityFragment : Fragment() {

    private var _binding: FragmentEnterCityBinding? = null
    val binding: FragmentEnterCityBinding
        get() = _binding!!

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

        binding.button.setOnClickListener {
            if (!binding.editText.text.isNullOrEmpty()) {

                val sdk = WeatherSDKManager.getInstance()

                val fragment = sdk.createWeatherFragment(binding.editText.text.toString())
                activity?.supportFragmentManager
                    ?.beginTransaction()?.replace(R.id.container, fragment)
                    ?.commit()
            }
        }


        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.textInputLayout.endIconMode = if (s.isNullOrEmpty()) {
                    TextInputLayout.END_ICON_NONE
                } else {
                    TextInputLayout.END_ICON_CUSTOM
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                Unit
        })

        binding.textInputLayout.setEndIconOnClickListener {
            binding.editText.text?.clear()
        }
    }
}