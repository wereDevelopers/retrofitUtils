package com.weredev.retrofitUtils

import android.util.Log
import com.weredev.retrofitUtils.exception.NetworkOperationException
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

object RetrofitUtils {

    /**
     * @param call network call
     */

    @Throws(NetworkOperationException::class)
    fun <T> execute(call: Call<T>): Response<T> {
        return executeCall(call)
    }

    fun <T> executeWith304(call: Call<T>): Pair<Response<T>, Boolean> {
        val response = executeCall(call)
        return when (response.raw().networkResponse?.code) {
            HttpURLConnection.HTTP_OK -> {
                Log.d(this.javaClass.simpleName,"Network -> executeWith304 MODIFIED")
                Pair(response, true)
            }
            HttpURLConnection.HTTP_NOT_MODIFIED -> {
                Log.d(this.javaClass.simpleName,"Network -> executeWith304 NOT_MODIFIED")
                Pair(response, false)
            }
            else -> throw NetworkOperationException(response.code(), response.errorBody()?.string() ?: "")
        }
    }

    fun <T> executeWithOneRetryWhen404(call: Call<T>, retryCall: Call<T>): Pair<Response<T>, Boolean> {
        val response = call.execute()
        return when (response.raw().networkResponse?.code) {
            HttpURLConnection.HTTP_OK -> {
                Log.d(this.javaClass.simpleName,"Network -> executeWith304 MODIFIED")
                Pair(response, true)
            }
            HttpURLConnection.HTTP_NOT_MODIFIED -> {
                Log.d(this.javaClass.simpleName,"Network -> executeWith304 NOT_MODIFIED")
                Pair(response, false)
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                return executeWith304(retryCall)
            }
            else -> throw NetworkOperationException(response.code(), response.errorBody()?.string() ?: "")
        }
    }


    @Throws(NetworkOperationException::class)
    fun <T> executeLight(call: Call<T>): Boolean {
        return executeLightCall(call)
    }

    private fun <T> executeLightCall(call: Call<T>): Boolean {
        var statusCode = -1
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return true
            } else {
                statusCode = response.code()
                throw NetworkOperationException(statusCode, response.errorBody()?.string() ?:"")
            }
        } catch (e: Exception) {
            throw NetworkOperationException(statusCode, e.message ?: e.toString())
        }
    }


    private fun <T> executeCall(call: Call<T>): Response<T> {
        var statusCode = -1
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response
            } else {
                statusCode = response.code()
                throw NetworkOperationException(response.code(), response.errorBody()?.string()?:"")
            }
        } catch (e: Exception) {
            e.cause
            throw NetworkOperationException(statusCode, e.message ?: e.toString())
        }
    }

//    fun <T> Response<BaseAPIResponse<T>>.checkAndParse(): T {
//        if (this.isSuccessful && this.body() != null) {
//            val response = this.body() as BaseAPIResponse<T>
//            if (response.result == 1) {
//                return response.data
//            } else {
//                throw NetworkOperationException("Network error [${response.description}")
//            }
//        } else {
//            throw NetworkOperationException("Network error [${this.errorBody()}")
//        }
//    }

    fun <T> Response<T>.checkAndParseResponse(): T {
        if (this.body() != null) {
            return this.body()!!
        } else {
            throw NetworkOperationException(1, "Network error [${this.code()} : ${this.errorBody()}")
        }
    }

}
