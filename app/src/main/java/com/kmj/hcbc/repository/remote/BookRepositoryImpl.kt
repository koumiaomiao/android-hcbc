package com.kmj.hcbc.repository.remote

import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.BookRepository
import com.kmj.hcbc.repository.remote.api.BookApiService
import com.kmj.hcbc.repository.remote.network.Resource
import com.kmj.hcbc.repository.remote.network.resourceOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val service: BookApiService
) : BookRepository {
    override suspend fun fetchAllBooks(): Resource<List<Book>?> {
        return resourceOf {
            service.fetchAllBooks()
        }
    }

    override suspend fun createBook(book: Book): Resource<Book?> {
        return resourceOf {
            service.createBook(book)
        }
    }
}
