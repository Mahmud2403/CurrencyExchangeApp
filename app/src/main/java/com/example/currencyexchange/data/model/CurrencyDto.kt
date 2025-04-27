package com.example.currencyexchange.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDto(
    val disclaimer: String,
    val date: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)