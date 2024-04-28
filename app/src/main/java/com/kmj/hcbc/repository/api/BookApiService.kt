package com.kmj.hcbc.repository.api

import com.kmj.hcbc.model.Book
import retrofit2.http.GET

interface BookApiService {
    @GET("/api/books")
    suspend fun fetchAllBooks(): List<Book>?
}
