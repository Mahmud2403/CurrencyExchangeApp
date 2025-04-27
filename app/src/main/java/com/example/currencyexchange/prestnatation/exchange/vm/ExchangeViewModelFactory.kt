package com.example.currencyexchange.prestnatation.exchange.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyexchange.data.storage.BalanceStorage
import com.example.currencyexchange.domain.repository.ExchangeRepository
import javax.inject.Inject

class ExchangeViewModelFactory @Inject constructor(
    private val repository: ExchangeRepository,
    private val balanceStorage: BalanceStorage,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeViewModel::class.java)) {
            return ExchangeViewModel(
                repository,
                balanceStorage
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

