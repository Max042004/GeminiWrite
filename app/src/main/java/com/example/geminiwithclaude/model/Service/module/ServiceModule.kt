package com.example.geminiwithclaude.model.Service.module

import com.example.geminiwithclaude.model.Service.AccountService
import com.example.geminiwithclaude.model.Service.StorageService
import com.example.geminiwithclaude.model.impl.AccountServiceImpl
import com.example.geminiwithclaude.model.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}