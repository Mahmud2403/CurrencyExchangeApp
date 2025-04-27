package com.example.currencyexchange.prestnatation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyexchange.App
import com.example.currencyexchange.R
import com.example.currencyexchange.prestnatation.exchange.ExchangeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        App.app.appComponent.inject(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        window.setSt(ContextCompat.getColor(this, R.color.teal_700)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ExchangeFragment())
                .commit()
        }
    }
}