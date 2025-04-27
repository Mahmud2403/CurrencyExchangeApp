package com.example.currencyexchange.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class BalanceStorageImpl  @Inject constructor(
    private val context: Context
): BalanceStorage {
    private val prefs: SharedPreferences
        get() = context.getSharedPreferences("balances_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()
    override fun getBalances(): Single<Map<String, Double>> {
        return Single.fromCallable {
            val json = prefs.getString("balances", null)
            if (json.isNullOrEmpty()) {
                emptyMap()
            } else {
                val type = object : TypeToken<Map<String, Double>>() {}.type
                gson.fromJson(json, type)
            }
        }
    }

    override fun saveBalances(balances: Map<String, Double>): Completable {
        return Completable.fromCallable {
            val json = gson.toJson(balances)
            prefs.edit().putString("balances", json).apply()
        }
    }

}