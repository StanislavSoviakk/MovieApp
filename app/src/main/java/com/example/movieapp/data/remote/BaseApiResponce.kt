package com.example.movieapp.data.remote

import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = api()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(data = body)
                } else {
                    errorMessage("Body is empty")
                }
            } else
                errorMessage(errorMessage = "${response.code()} ${response.message()}")
        } catch (e: Exception) {
            errorMessage(errorMessage = e.message.toString())
        }
    }

    private fun <T> errorMessage(errorMessage: String): NetworkResult.Error<T> =
        NetworkResult.Error(data = null, message = "Api call failed: $errorMessage")
}