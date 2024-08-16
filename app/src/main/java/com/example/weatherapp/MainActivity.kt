package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.ui.EnterCityFragment
import com.example.weathersdk.WeatherSDKManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WeatherSDKManager.initializeSDK("")


        supportFragmentManager.beginTransaction().replace(R.id.container, EnterCityFragment())
            .commit()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                WeatherSDKManager.getInstance().observeWeatherSdkEvents().collect {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            EnterCityFragment()
                        ).commit()
                }
            }
        }

    }

}