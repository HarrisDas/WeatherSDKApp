package com.example.weathersdk.internal.common


internal sealed interface Result<out S, out F> {
    data class Success<S>(val data: S) : Result<S, Nothing>
    data class Failure<F>(val reason: F) : Result<Nothing, F>
}