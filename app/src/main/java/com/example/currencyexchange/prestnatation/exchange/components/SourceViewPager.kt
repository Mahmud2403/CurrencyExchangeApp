package com.example.currencyexchange.prestnatation.exchange.components

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.ItemCurrencyCardBinding
import com.example.currencyexchange.prestnatation.exchange.model.CurrencyInfo

class SourceViewPager :
    ListAdapter<CurrencyInfo, SourceViewPager.CurrencyViewHolder>(DiffCallback()) {

    private var targetRate: Double = 0.0
    private var targetSymbol: String = ""
    var sourceAmount: Double? = null

    var onAmountChanged: ((Double) -> Unit)? = null


    inner class CurrencyViewHolder(private val binding: ItemCurrencyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var textWatcher: TextWatcher? = null

        fun bind(item: CurrencyInfo) = with(binding) {
            val context = itemView.context
            tvCurrency.text = item.code
            tvBalance.text = context.getString(R.string.tv_balance, item.amount, item.symbol)

            textWatcher?.let {
                etAmount.removeTextChangedListener(it)
            }

            val newAmountText = if (sourceAmount == null || sourceAmount == 0.0) ""
            else if (sourceAmount!! % 1.0 == 0.0) sourceAmount!!.toInt().toString()
            else sourceAmount!!.toString()

            if (etAmount.text.toString() != newAmountText) {
                etAmount.setText(newAmountText)
                etAmount.setSelection(newAmountText.length)
            }

            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) = Unit

                override fun afterTextChanged(editable: Editable?) {
                    sourceAmount = editable?.toString()?.toDoubleOrNull() ?: 0.0
                    onAmountChanged?.invoke(sourceAmount ?: 0.0)
                }
            }
            etAmount.addTextChangedListener(textWatcher)
            tvRate.text = context.getString(R.string.exchange_rate_format, item.symbol, targetRate, targetSymbol)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrencyCardBinding.inflate(inflater, parent, false)
        val params = binding.root.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class DiffCallback : DiffUtil.ItemCallback<CurrencyInfo>() {

        override fun areItemsTheSame(
            oldItem: CurrencyInfo,
            newItem: CurrencyInfo,
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: CurrencyInfo,
            newItem: CurrencyInfo,
        ): Boolean {
            return oldItem.code == newItem.code
        }
    }

    fun updateTargetRate(newRate: Double, newSymbol: String) {
        if (this.targetRate != newRate || this.targetSymbol != newSymbol) {
            this.targetRate = newRate
            this.targetSymbol = newSymbol
            notifyItemRangeChanged(0, itemCount, PAYLOAD_RATE_UPDATE)
        }
    }

    fun clearAmount() {
        sourceAmount = null
        notifyDataSetChanged()
    }

    companion object {
        private const val PAYLOAD_RATE_UPDATE = "rate_update"
    }
}