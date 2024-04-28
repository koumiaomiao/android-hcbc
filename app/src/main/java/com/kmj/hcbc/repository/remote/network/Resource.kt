package com.kmj.hcbc.repository.remote.network

import com.google.gson.JsonObject

class Resource<T> private constructor(
    val status: State,
    val data: T?,
    val errorBody: BffErrorResponse?,
    val throwable: Throwable?
) {
    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(State.SUCCESS, data, null, null)
        }

        fun <T> error(errorBody: BffErrorResponse?, throwable: Throwable?): Resource<T> {
            return Resource(State.ERROR, null, errorBody, throwable)
        }
    }
}

enum class State {
    SUCCESS,
    ERROR
}

data class BffErrorResponse(val code: String?, val message: String?, val error: JsonObject?)
