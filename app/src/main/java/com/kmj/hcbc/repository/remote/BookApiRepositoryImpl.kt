package com.kmj.hcbc.repository.remote

import com.kmj.hcbc.model.Book
import com.kmj.hcbc.repository.BookApiRepository
import com.kmj.hcbc.repository.remote.api.BookApiService
import com.kmj.hcbc.repository.remote.network.Resource
import com.kmj.hcbc.repository.remote.network.resourceOf

class BookApiRepositoryImpl(private val service: BookApiService) : BookApiRepository {
    override suspend fun fetchAllBooks(): Resource<List<Book>?> {
        return resourceOf {
            service.fetchAllBooks()
        }
    }
}
