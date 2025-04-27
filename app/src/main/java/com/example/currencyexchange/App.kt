package com.example.currencyexchange

import android.app.Application
import com.example.currencyexchange.data.api.di.NetworkWrapper
import com.example.currencyexchange.data.storage.di.StorageWrapper

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initNetwork()
        initStorage()
    }

    private fun initNetwork() {
        NetworkWrapper.initComponent()
    }

    private fun initStorage() {
        StorageWrapper.initComponent(this)
    }
}