package com.example.weathersdk.internal

internal class ApiKeyManager private constructor() {
    private lateinit var apiKey: String

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