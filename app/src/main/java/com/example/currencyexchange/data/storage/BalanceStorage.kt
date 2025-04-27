package com.example.currencyexchange.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface BalanceStorage {

    fun getBalances(): Single<Map<String, Double>>

    fun saveBalances(balances: Map<String, Double>): Completable
}
