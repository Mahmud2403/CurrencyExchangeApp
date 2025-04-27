package com.example.currencyexchange.prestnatation.exchange.components

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.ItemCurrencyCardBinding
import com.example.currencyexchange.prestnatation.exchange.model.CurrencyInfo

class TargetViewPager() : ListAdapter<CurrencyInfo, TargetViewPager.CurrencyViewHolder>(DiffCallback()) {

    private var sourceRate: Double = 0.0
    private var sourceSymbol: String = ""
    private var sourceAmount: Double? = null

    inner class CurrencyViewHolder(private val binding: ItemCurrencyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: CurrencyInfo) = with(binding) {
            val context = itemView.context
            tvCurrency.text = item.code
            tvBalance.text = context.getString(R.string.tv_balance, item.amount, item.symbol)
            sourceAmount?.let {
                etAmount.hint = it.toString()
            }

            tvRate.text = context.getString(R.string.exchange_rate_format, item.symbol, sourceRate, sourceSymbol)
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

    fun updateSourceRate(newRate: Double, newSymbol: String, newAmount: Double) {
        if (this.sourceRate != newRate || this.sourceSymbol != newSymbol || this.sourceAmount != newAmount) {
            this.sourceRate = newRate
            this.sourceSymbol = newSymbol
            this.sourceAmount = newAmount
            notifyItemRangeChanged(0, itemCount, PAYLOAD_RATE_UPDATE)
        }
    }

    companion object {
        private const val PAYLOAD_RATE_UPDATE = "rate_update"
    }
}

