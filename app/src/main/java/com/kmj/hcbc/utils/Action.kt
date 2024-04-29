package com.kmj.hcbc.utils

sealed class Action {
    class FetchDataError(val message: String?) : Action()
    object NetworkError : Action()
}