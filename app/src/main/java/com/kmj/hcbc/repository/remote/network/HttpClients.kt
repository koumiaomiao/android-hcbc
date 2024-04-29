package com.kmj.hcbc.repository.remote.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpClients {

    private const val HTTP_REQUEST_TIMEOUT = 30L
    private const val BFF_BASE_URL = "http://3.26.200.133:8088"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BFF_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}