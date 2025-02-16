package com.hassan.duckit.di

import com.hassan.duckit.data.repository.AuthRepositoryImpl
import com.hassan.duckit.data.repository.PostsRepositoryImpl
import com.hassan.duckit.domain.repository.AuthRepository
import com.hassan.duckit.domain.repository.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindPostsRepository(
        postsRepositoryImpl: PostsRepositoryImpl
    ): PostsRepository
}
