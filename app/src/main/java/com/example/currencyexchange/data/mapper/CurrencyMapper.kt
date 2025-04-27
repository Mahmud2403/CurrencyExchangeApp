package com.example.currencyexchange.data.mapper

import com.example.currencyexchange.data.model.CurrencyDto
import com.example.currencyexchange.domain.model.CurrencyEntity

fun CurrencyDto.toEntity(): CurrencyEntity =
    CurrencyEntity(
        date = date,
        timestamp = timestamp,
        base = base,
        rates = rates,
    )