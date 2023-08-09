package com.example.userregistration.api

sealed class Envelope<T> {

    class Error<T>(val error: ErrorModel) : Envelope<T>()

    data class Success<T>(val data: T) : Envelope<T>()

    class Loading<T> : Envelope<T>()

    companion object {
        fun <T> loading() = Loading<T>()

        fun <T> success(data: T) = Success<T>(data)

        fun <T> error(error: ErrorModel) = Error<T>(error)
    }

}