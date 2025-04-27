package com.example.currencyexchange.data.storage.di

import android.content.Context
import androidx.annotation.MainThread
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.currencyexchange.data.api.NetworkApi
import com.example.currencyexchange.data.api.di.DaggerNetworkComponent
import com.example.currencyexchange.data.api.di.NetworkComponent
import com.example.currencyexchange.data.storage.BalanceStorage

object StorageWrapper {

    private var component: StorageComponent? = null

    @MainThread
    fun getStorage(): BalanceStorage = getComponent().moduleStorage()

    @MainThread
    fun initComponent(context: Context) {
        component = DaggerStorageComponent
            .builder()
            .context(context)
            .build()
    }

    private fun getComponent(): StorageComponent = requireNotNull(component)
}