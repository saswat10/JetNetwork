package com.saswat10.jetnetwork.di

import com.saswat10.jetnetwork.data.AuthRepositoryImpl
import com.saswat10.jetnetwork.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        implementation: AuthRepositoryImpl,
    ): AuthRepository

}

