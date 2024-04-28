package com.kmj.hcbc.repository.remote.network

import com.google.gson.Gson
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun <T> resourceOf(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> T
): Resource<T> {
    return withContext(context) {
        try {
            Resource.success(block())
        } catch (exception: Exception) {
            if (exception is HttpException) {
                handleErrorResource(exception)
            } else {
                Resource.error(null, exception)
            }
        }
    }
}

private fun <T> handleErrorResource(exception: HttpException): Resource<T> {
    return try {
        val response = exception.response()?.errorBody()?.charStream()?.use {
            Gson().fromJson(it, BffErrorResponse::class.java)
        }
        Resource.error(response, exception)
    } catch (exception: Exception) {
        Resource.error(null, exception)
    }
}
