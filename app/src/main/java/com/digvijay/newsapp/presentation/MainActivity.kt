package com.digvijay.newsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.digvijay.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
    }
}