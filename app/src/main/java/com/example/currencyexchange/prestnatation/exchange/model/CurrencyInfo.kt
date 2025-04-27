package com.example.currencyexchange.prestnatation.exchange.model

data class CurrencyInfo(
    val code: String,
    val symbol: String,
    val rate: Double,
    val amount: Double,
)
