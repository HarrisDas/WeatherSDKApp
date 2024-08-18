package com.example.weathersdk.internal

internal class ApiKeyManager private constructor() {
    var apiKey: String? = null
        private set


    companion object {
        @Volatile
        private var instance: ApiKeyManager? = null

        fun getInstance(): ApiKeyManager {
            return instance ?: synchronized(this) {
                instance ?: ApiKeyManager().also { instance = it }
            }
        }
    }

    fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

}