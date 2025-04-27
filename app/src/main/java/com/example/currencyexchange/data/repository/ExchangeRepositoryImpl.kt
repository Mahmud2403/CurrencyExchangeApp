package com.example.currencyexchange.data.repository

import com.example.currencyexchange.data.api.RestApi
import com.example.currencyexchange.data.mapper.toEntity
import com.example.currencyexchange.domain.model.CurrencyEntity
import com.example.currencyexchange.domain.repository.ExchangeRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val api: RestApi
) : ExchangeRepository {
    override fun getLatestRates(): Single<CurrencyEntity> {
        return api.getLatestRates().map { it.toEntity() }
    }
}