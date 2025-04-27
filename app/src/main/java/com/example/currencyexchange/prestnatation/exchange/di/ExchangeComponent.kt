package com.example.currencyexchange.prestnatation.exchange.di

import com.example.currencyexchange.data.storage.BalanceStorage
import com.example.currencyexchange.di.scopes.FragmentScope
import com.example.currencyexchange.prestnatation.exchange.ExchangeCoreDependencies
import com.example.currencyexchange.prestnatation.exchange.ExchangeFragment
import dagger.Component

@FragmentScope
@Component(
    modules = [ExchangeModule::class],
    dependencies = [ExchangeCoreDependencies::class]
)
interface ExchangeComponent {

    @Component.Factory
    interface Factory {
        fun create(coreDependencies: ExchangeCoreDependencies): ExchangeComponent
    }

    fun inject(fragment: ExchangeFragment)
}