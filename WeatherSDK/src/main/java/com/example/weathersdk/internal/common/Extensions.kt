package com.example.weathersdk.internal.common


import com.example.weathersdk.internal.data.model.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

internal suspend fun <T> handleApiResponse(
    apiCall: suspend () -> Response<T>
): Result<T, String> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            } ?: Result.Failure("Response body is null")
        } else {
            val errorResponse: ErrorResponse? = Gson().fromJson(
                response.errorBody()?.charStream(),
                object : TypeToken<ErrorResponse>() {}.type
            )

            errorResponse?.let {

                Result.Failure(it.error)

            } ?: run {
                Result.Failure("Error: ${response.code()} - ${response.message()}")
            }
        }
    } catch (e: Exception) {
        Result.Failure("Exception: ${e.localizedMessage}")
    }
}

