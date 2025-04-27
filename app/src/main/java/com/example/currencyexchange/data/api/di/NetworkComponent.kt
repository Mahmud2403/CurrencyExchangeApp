package com.example.currencyexchange.data.api.di

import com.example.currencyexchange.data.api.NetworkApi
import com.example.currencyexchange.data.api.NetworkApiImpl
import com.example.currencyexchange.di.scopes.NetworkScope
import dagger.BindsInstance
import dagger.Component

@NetworkScope
@Component(modules = [NetworkModule::class])
internal interface NetworkComponent {

    @Component.Factory
    interface Factory {

        fun create(): NetworkComponent
    }

    fun inject(apiImpl: NetworkApiImpl)

    fun moduleApi(): NetworkApi
}