package com.example.currencyexchange.prestnatation.exchange

import com.example.currencyexchange.data.api.RestApi
import com.example.currencyexchange.data.api.di.NetworkWrapper
import com.example.currencyexchange.data.storage.BalanceStorage
import com.example.currencyexchange.data.storage.di.StorageWrapper
import com.example.currencyexchange.prestnatation.exchange.di.DaggerExchangeComponent
import com.example.currencyexchange.prestnatation.exchange.di.ExchangeComponent
import com.example.currencyexchange.prestnatation.exchange.di.ExchangeModule

object ExchangeFeature {
    private var component: ExchangeComponent? = null

    fun getComponent(): ExchangeComponent =
        component ?: run {
            component = DaggerExchangeComponent.factory().create(
                coreDependencies = ExchangeCoreDependenciesDelegate()
            )
            requireNotNull(component)
        }
}

interface ExchangeCoreDependencies {
    fun provideRestApi(): RestApi
    fun provideStorage(): BalanceStorage
}

class ExchangeCoreDependenciesDelegate : ExchangeCoreDependencies {
    override fun provideRestApi(): RestApi {
        return NetworkWrapper.getApi().provideApiClass()
    }

    override fun provideStorage(): BalanceStorage {
        return StorageWrapper.getStorage()
    }
}