package com.kmj.hcbc.utils

sealed class Action {
    data object FetchDataError : Action()
    data object CreateDataError : Action()
    data object DeleteDataError : Action()
    data object UpdateDataError : Action()
    data object SearchDataError : Action()
}
