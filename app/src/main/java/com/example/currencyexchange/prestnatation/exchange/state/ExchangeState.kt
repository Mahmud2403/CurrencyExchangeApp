package com.example.currencyexchange.prestnatation.exchange.state

data class CurrencyState(
    val amountTop: String = "",
    val amountBottom: String = "",
    val selectedTopCurrency: String = "USD",
    val selectedBottomCurrency: String = "EUR",
    val rates: Map<String, Double> = emptyMap()
)