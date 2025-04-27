package com.example.currencyexchange.data.storage.di

import android.content.Context
import com.example.currencyexchange.data.storage.BalanceStorage
import com.example.currencyexchange.prestnatation.exchange.ExchangeFragment
import com.example.currencyexchange.prestnatation.exchange.di.ExchangeModule
import dagger.BindsInstance
import dagger.Component


@Component(modules = [StorageModule::class])
interface StorageComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): StorageComponent
    }

    fun moduleStorage(): BalanceStorage
}