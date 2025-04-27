package com.example.currencyexchange.util

import java.util.Currency

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun getCurrencySymbol(code: String): String {
    return Currency.getInstance(code).symbol
}
