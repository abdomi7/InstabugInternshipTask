package com.example.instabuginternshiptask.di

import com.example.instabuginternshiptask.data.repository.QueryUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideQueryUtils(): QueryUtils = QueryUtils()
}