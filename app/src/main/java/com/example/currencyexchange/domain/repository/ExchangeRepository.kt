package com.example.currencyexchange.domain.repository

import com.example.currencyexchange.domain.model.CurrencyEntity
import io.reactivex.rxjava3.core.Single

interface ExchangeRepository {
    fun getLatestRates(): Single<CurrencyEntity>
}