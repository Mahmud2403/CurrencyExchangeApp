package com.example.currencyexchange.prestnatation.exchange.vm

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyexchange.data.storage.BalanceStorage
import com.example.currencyexchange.domain.repository.ExchangeRepository
import com.example.currencyexchange.prestnatation.exchange.model.CurrencyInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.math.roundToInt

class ExchangeViewModel @Inject constructor(
    private val repository: ExchangeRepository,
    private val balanceStorage: BalanceStorage,
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val combinedCurrencies = MediatorLiveData<Pair<List<CurrencyInfo>, List<CurrencyInfo>>>()
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 30_000L

    private val selectedKeys = listOf("USD", "EUR", "GBP", "AUD", "CNY")
    private val symbols = mapOf(
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "AUD" to "A$",
        "CNY" to "¥"
    )

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _balances = MutableLiveData<Map<String, Double>>()

    private val _target = MutableLiveData<List<CurrencyInfo>>()

    private val _source = MutableLiveData<List<CurrencyInfo>>()

    private val _insufficientFunds = MutableLiveData<String>()
    val insufficientFunds: LiveData<String> = _insufficientFunds
    init {
        loadBalances()
        fetchRates()
        startPeriodicRateUpdates()

        combinedCurrencies.addSource(_source) { sourceList ->
            val targetList = _target.value
            if (sourceList != null && targetList != null) {
                combinedCurrencies.value = sourceList to targetList
            }
        }
        combinedCurrencies.addSource(_target) { targetList ->
            val sourceList = _source.value
            if (sourceList != null && targetList != null) {
                combinedCurrencies.value = sourceList to targetList
            }
        }
    }

    private fun loadBalances() {
        balanceStorage.getBalances()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ savedBalances ->
                if (savedBalances.isEmpty()) {
                    val initialBalances = selectedKeys.associateWith { 100.0 }
                    _balances.value = initialBalances
                    saveBalances(initialBalances)
                } else {
                    _balances.value = savedBalances
                }
            }, { throwable ->
                _error.value = "Ошибка загрузки баланса: ${throwable.localizedMessage}"
            })
            .also {
                disposables.add(it)
            }
    }

    private fun saveBalances(newBalances: Map<String, Double>) {
        balanceStorage.saveBalances(newBalances)
            .subscribeOn(Schedulers.io())
            .subscribe({}, { throwable ->
                _error.postValue("Ошибка сохранения баланса: ${throwable.localizedMessage}")
            }).also {
                disposables.add(it)
            }
    }

    private fun fetchRates() {
        repository.getLatestRates()
            .doOnSubscribe { _loading.postValue(true) }
            .doFinally { _loading.postValue(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { entity ->
                    val currency = entity.rates
                        .filterKeys { it in selectedKeys }
                        .map { (code, rate) ->
                            CurrencyInfo(
                                code = code,
                                symbol = symbols[code] ?: code,
                                rate = rate,
                                amount = _balances.value?.get(code) ?: 0.0
                            )
                        }
                    _source.value = currency
                    _target.value = currency
                },
                { throwable ->
                    when (throwable) {
                        is SocketTimeoutException -> _error.value = "Сервер не отвечает. Попробуйте ещё раз."
                        else -> _error.value = "Ошибка при получении курсов: ${throwable.localizedMessage}"
                    }
                }
            )
            .also { disposables.add(it) }
    }

    fun exchangeCurrency(fromCurrency: String, toCurrency: String, sourceAmount: Double, targetAmount: Double) {
        val currentBalance = _balances.value ?: return
        val newBalances = currentBalance.toMutableMap()

        val fromAmount = currentBalance[fromCurrency] ?: 0.0
        if (fromAmount < sourceAmount) {
            _insufficientFunds.value = "Недостаточно средств для обмена $fromCurrency на $toCurrency"
        } else {
            newBalances[fromCurrency] = fromAmount - sourceAmount
            newBalances[toCurrency] = (newBalances[toCurrency] ?: 0.0) + targetAmount

            _balances.value = newBalances
            saveBalances(newBalances)
        }
    }

    fun clearError() {
        _insufficientFunds.value = ""
    }


    private fun startPeriodicRateUpdates() {
        val runnable = object : Runnable {
            override fun run() {
                fetchRates()
                handler.postDelayed(this, updateInterval)
            }
        }
        handler.post(runnable)
    }

    fun getAvailableAccounts(): List<CurrencyInfo> {
        val currentBalances = _balances.value ?: emptyMap()
        return selectedKeys.map { code ->
            CurrencyInfo(
                code = code,
                symbol = symbols[code] ?: code,
                rate = 1.0, // здесь курс нам не важен для диалога
                amount = currentBalances[code] ?: 0.0
            )
        }
    }

    fun getUpdatedBalance(code: String): Double {
        return _balances.value?.get(code) ?: 0.0
    }

    fun convertCurrency(amount: Double, from: Double, to: Double): Double {
        return (amount * (1 / from) * to * 10.0).roundToInt() / 10.0
    }

    fun convertCurrency(from: Double, to: Double): Double {
        return (1 / from) * to
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
