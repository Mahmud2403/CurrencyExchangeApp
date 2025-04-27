package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.model.CurrencyDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface RestApi {
    @GET("latest.js")
    fun getLatestRates(): Single<CurrencyDto>
}