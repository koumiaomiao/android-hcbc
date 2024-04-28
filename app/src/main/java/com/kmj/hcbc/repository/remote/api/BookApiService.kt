package com.kmj.hcbc.repository.remote.api

import com.kmj.hcbc.model.Book
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface BookApiService {
    @GET("/api/books")
    suspend fun fetchAllBooks(): List<Book>?

    @POST("/api/books")
    suspend fun createBook(@Body book: Book): Book?

    @DELETE("/api/books/{id}")
    suspend fun deleteBookById(id: String): Unit?

    @PUT("/api/books/{id}")
    suspend fun updateBook(@Body book: Book): Book?
}
