package com.example.currencyexchange.domain.model

data class CurrencyEntity(
    val date: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
