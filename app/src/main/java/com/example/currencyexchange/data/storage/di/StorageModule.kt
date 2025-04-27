package com.example.currencyexchange.data.storage.di

import android.content.Context
import com.example.currencyexchange.data.storage.BalanceStorage
import com.example.currencyexchange.data.storage.BalanceStorageImpl
import dagger.Module
import dagger.Provides

@Module
class StorageModule {
    @Provides
    fun provideBalanceStorage(
        context: Context
    ): BalanceStorage {
        return BalanceStorageImpl(context)
    }
}