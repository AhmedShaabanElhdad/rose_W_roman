package com.smartzone.myapp.data.utils

sealed class Result<out T : Any> {
    class Success<out T : Any>(val data: T) : Result<T>()
    class Failure(val exception: String) : Result<Nothing>()
}
