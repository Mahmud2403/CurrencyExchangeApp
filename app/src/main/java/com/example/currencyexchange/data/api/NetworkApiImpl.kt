package com.example.currencyexchange.data.api

import okhttp3.OkHttpClient
import javax.inject.Inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkApiImpl @Inject constructor(
    private val okhttp3Client: OkHttpClient
) : NetworkApi {


    override fun provideApiClass(): RestApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttp3Client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(RestApi::class.java)
    }

    private companion object {
        private const val BASE_URL = "https://www.cbr-xml-daily.ru/"
    }
}