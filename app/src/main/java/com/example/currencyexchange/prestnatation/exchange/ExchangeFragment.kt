package com.example.currencyexchange.prestnatation.exchange

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.FragmentExchangeBinding
import com.example.currencyexchange.prestnatation.exchange.components.SourceViewPager
import com.example.currencyexchange.prestnatation.exchange.components.TargetViewPager
import com.example.currencyexchange.prestnatation.exchange.model.CurrencyInfo
import com.example.currencyexchange.prestnatation.exchange.vm.ExchangeViewModel
import com.example.currencyexchange.prestnatation.exchange.vm.ExchangeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ExchangeFragment : Fragment() {

    private var _binding: FragmentExchangeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ExchangeViewModelFactory
    private val viewModel: ExchangeViewModel by viewModels { viewModelFactory }

    private lateinit var sourceAdapter: SourceViewPager
    private lateinit var targetAdapter: TargetViewPager

    private var sourceRate = 0.0
    private var targetRate = 0.0

    private var sourceSymbol = ""
    private var targetSymbol = ""

    private var sourceCode = ""
    private var targetCode = ""

    private var sourceAmount: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExchangeFeature.getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentExchangeBinding
        .inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupListeners()
        observeViewModel()
    }

    private fun setupAdapters() {
        sourceAdapter = SourceViewPager()
        targetAdapter = TargetViewPager()

        binding.viewPagerCurrenciesSource.adapter = sourceAdapter
        binding.viewPagerCurrenciesTarget.adapter = targetAdapter

        binding.viewPagerCurrenciesSource.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                sourceAdapter.currentList.getOrNull(position)?.let { currency ->
                    sourceRate = currency.rate
                    sourceSymbol = currency.symbol
                    sourceCode = currency.code
                    updateConversion()
                }
            }
        })

        binding.viewPagerCurrenciesTarget.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                targetAdapter.currentList.getOrNull(position)?.let { currency ->
                    targetRate = currency.rate
                    targetSymbol = currency.symbol
                    targetCode = currency.code
                    updateConversion()
                }
            }
        })

        sourceAdapter.onAmountChanged = { amount ->
            sourceAmount = amount
            updateConversion()
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.isVisible = isLoading
                binding.exchangell.isVisible = !isLoading
                binding.errorFL.isVisible = false
            }

            error.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    binding.progressBar.isVisible = false
                    binding.exchangell.isVisible = false
                    binding.errorFL.isVisible = true
                    binding.errorTv.text = errorMessage
                }
            }

            combinedCurrencies.observe(viewLifecycleOwner) { (sourceList, targetList) ->
                if (sourceList.isNotEmpty() && targetList.isNotEmpty()) {
                    sourceAdapter.submitList(sourceList)
                    targetAdapter.submitList(targetList)

                    val sourceCurrency = sourceList.first()
                    val targetCurrency = targetList.first()

                    sourceRate = sourceCurrency.rate
                    sourceSymbol = sourceCurrency.symbol
                    sourceCode = sourceCurrency.code

                    targetRate = targetCurrency.rate
                    targetSymbol = targetCurrency.symbol
                    targetCode = targetCurrency.code

                    updateConversion()
                }
            }

            insufficientFunds.observe(viewLifecycleOwner) { message ->
                message?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnExchange.setOnClickListener {
            val amount = sourceAmount ?: 0.0
            viewModel.exchangeCurrency(sourceCode, targetCode, amount, targetAmount())
            showExchangeDialog()
        }
    }

    private fun updateConversion() {
        val sourceToTarget = viewModel.convertCurrency(sourceAmount ?: 0.0, sourceRate, targetRate)
        val targetToSource = viewModel.convertCurrency(1.0, targetRate, sourceRate)

        sourceAdapter.updateTargetRate(sourceToTarget, targetSymbol)
        targetAdapter.updateSourceRate(targetToSource, sourceSymbol, targetAmount())

        binding.textExchangeRate.text = getString(
            R.string.exchange_rate_format,
            sourceSymbol,
            targetToSource,
            targetSymbol
        )
    }

    private fun showExchangeDialog() {
        val amount = sourceAmount ?: 0.0
        val balance = viewModel.getUpdatedBalance(targetCode)
        val accounts = viewModel.getAvailableAccounts()

        val message = buildDialogMessage(amount, targetCode, balance, accounts)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.exchange_success_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> clearInput() }
            .show()
    }


    private fun buildDialogMessage(
        exchangedAmount: Double,
        targetCurrency: String,
        updatedBalance: Double,
        availableAccounts: List<CurrencyInfo>
    ): SpannableString {
        val header = getString(
            R.string.receipt_message,
            String.format("%.2f", exchangedAmount),
            targetCurrency,
            String.format("%.2f", updatedBalance)
        )
        val accountsText = availableAccounts.joinToString("\n") { "${it.symbol}${String.format("%.2f", it.amount)}" }
        val fullText = "$header\n\n${getString(R.string.available_accounts)}\n$accountsText"

        return SpannableString(fullText).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, header.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun clearInput() {
        sourceAmount = null
        sourceAdapter.clearAmount()
        updateConversion()
    }

    private fun showExchangeErrorDialog(insufficientFunds: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(insufficientFunds)
            .setPositiveButton("Ok", { _, _ ->
                viewModel.clearError()
            })
            .show()
    }

    private fun targetAmount(): Double {
        return if (sourceAmount != null) {
            viewModel.convertCurrency(sourceAmount!!, sourceRate, targetRate)
        } else 0.0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
