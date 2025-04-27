package com.example.currencyexchange.prestnatation.exchange.di

import android.content.Context
import com.example.currencyexchange.data.repository.ExchangeRepositoryImpl
import com.example.currencyexchange.data.storage.BalanceStorage
import com.example.currencyexchange.domain.repository.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface ExchangeModule {

    @Binds
    fun provideRepository(impl: ExchangeRepositoryImpl):ExchangeRepository
}