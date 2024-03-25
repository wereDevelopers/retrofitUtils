package com.weredev.retrofitUtils.exception

import java.net.HttpURLConnection

class NetworkOperationException(errorCode: Int = -1, message: String) : Exception(message){

    val errorCause = when (errorCode) {
        1 -> PARSING_FAILED
        HttpURLConnection.HTTP_BAD_REQUEST -> BAD_REQUEST
        HttpURLConnection.HTTP_UNAUTHORIZED -> UNAUTHORIZED
        HttpURLConnection.HTTP_FORBIDDEN -> FORBIDDEN
        HttpURLConnection.HTTP_INTERNAL_ERROR -> INTERNAL_SERVER_ERROR
        HttpURLConnection.HTTP_BAD_GATEWAY -> BAD_GATEWAY
        HttpURLConnection.HTTP_UNAVAILABLE -> SERVICE_UNAVAILABLE
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> TIMEOUT
        HttpURLConnection.HTTP_NOT_FOUND -> HTTP_NOT_FOUND
        else -> {
            if (message == "timeout")
                TIMEOUT
            else
                UNKNOWN
        }
    }

    companion object {
        const val PARSING_FAILED = "PARSING_FAILED"
        const val BAD_REQUEST = "BAD_REQUEST"
        const val UNAUTHORIZED = "UNAUTHORIZED"
        const val FORBIDDEN = "FORBIDDEN"
        const val INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR"
        const val BAD_GATEWAY = "BAD_GATEWAY"
        const val SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE"
        const val TIMEOUT = "TIMEOUT"
        const val HTTP_NOT_FOUND = "HTTP_NOT_FOUND"
        const val UNKNOWN = "UNKNOWN"
    }
}