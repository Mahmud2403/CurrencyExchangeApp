package com.example.currencyexchange.prestnatation.exchange.listener

interface OnCurrencyCardInteractionListener {
    fun onAmountChanged(position: Int, newAmount: Double)
}