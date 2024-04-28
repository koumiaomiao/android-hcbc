package com.kmj.hcbc.di

import com.kmj.hcbc.repository.BookRepository
import com.kmj.hcbc.repository.remote.BookRepositoryImpl
import com.kmj.hcbc.repository.remote.api.BookApiService
import com.kmj.hcbc.repository.remote.network.HttpClients
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideBookRepositoryModule(bookRepository: BookRepositoryImpl): BookRepository
}

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideBookApiService(): BookApiService {
        return HttpClients.retrofit.create(BookApiService::class.java)
    }
}