package com.example.currencyexchange.data.api.di

import androidx.annotation.MainThread
import com.example.currencyexchange.data.api.NetworkApi


object NetworkWrapper {

    private var component: NetworkComponent? = null

    @MainThread
    fun getApi(): NetworkApi = getComponent().moduleApi()

    @MainThread
    fun initComponent() {
        component = DaggerNetworkComponent.factory().create()
    }

    private fun getComponent(): NetworkComponent = requireNotNull(component)
}