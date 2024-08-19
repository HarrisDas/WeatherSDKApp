package com.example.weathersdk

import androidx.fragment.app.Fragment
import com.example.weathersdk.internal.ApiKeyManager
import com.example.weathersdk.internal.WeatherSDKManagerImpl
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the core functionality of the Weather SDK.
 *
 * The [WeatherSDKManager] interface provides methods to create weather-related fragments,
 * observe weather events, and initialize the SDK with an API key.
 *
 * @interface WeatherSDKManager
 * @author Harris Das
 */
interface WeatherSDKManager {

    /**
     * Creates a new instance of [WeatherFragment] with the specified city name.
     *
     * This function initializes a [WeatherFragment] and sets up the necessary arguments
     * to display weather information for the given city. The city name is passed as an argument
     * in a [Bundle], which is then attached to the fragment.
     *
     * @param cityName The name of the city for which the weather information is to be displayed.
     * @return A [Fragment] configured to display the weather for the specified city.
     *
     * @author Harris Das
     */
    fun createWeatherFragment(cityName: String): Fragment

    /**
     * Observes weather-related events from the Weather SDK.
     *
     * This function returns a [Flow] of [WeatherSDKEvent]s, which can be collected to observe
     * various events emitted by the Weather SDK. The events are provided by an internal event
     * bus mechanism, allowing clients to react to internal events i.e when fragment is closed.
     *
     * @return A [Flow] of [WeatherSDKEvent]s emitted by the Weather SDK.
     *
     * @author Harris Das
     */
    fun observeWeatherSdkEvents(): Flow<WeatherSDKEvent>

    companion object {
        @Volatile
        private var instance: WeatherSDKManager? = null

        /**
         * Returns the singleton instance of [WeatherSDKManager].
         *
         * This function ensures that only one instance of [WeatherSDKManager] is created
         * and provides a global point of access to it. The instance is lazily initialized and
         * thread-safe using the `synchronized` block to avoid multiple instances being created
         * in a multi-threaded environment.
         *
         * @return The singleton instance of [WeatherSDKManager].
         *
         * @author Harris Das
         */
        @JvmStatic

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: WeatherSDKManagerImpl().also { instance = it }
        }

        /**
         * Initializes the Weather SDK with the provided API key.
         *
         * This function sets the API key required for the Weather SDK to function correctly.
         * It delegates the responsibility of storing the API key to the [ApiKeyManager].
         * The API key is essential for authenticating requests to the weather service.
         *
         * @param apiKey The API key used to authenticate the Weather SDK.
         *
         * @author Harris Das
         */
        @JvmStatic
        fun initializeSDK(apiKey: String) {
            ApiKeyManager.getInstance().setApiKey(apiKey)
        }
    }
}
