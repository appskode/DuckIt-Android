package com.hassan.duckit.di

import com.hassan.duckit.di.qualifier.IODispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
