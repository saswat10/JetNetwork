package com.saswat10.jetnetwork.di

import com.saswat10.jetnetwork.data.AuthRepositoryImpl
import com.saswat10.jetnetwork.data.PostRepositoryImpl
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds abstract fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds abstract fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository
}

