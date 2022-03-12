package com.digvijay.newsapp.framework

import android.app.Application
import android.content.Context
import com.digvijay.newsapp.data.CountryDataSource
import com.digvijay.newsapp.domain.NewsCountry

class CountryPrefsDataSource(private val application: Application) : CountryDataSource {

    override fun saveCountry(country: NewsCountry) {
        application.getSharedPreferences("prefs",Context.MODE_PRIVATE)
            .edit().putString("countryCode", country.code)
            .apply()
    }

    override fun getCountry(): NewsCountry {
        val countryCode = application.getSharedPreferences("prefs",Context.MODE_PRIVATE)
            .getString("countryCode", "")
        return when (countryCode) {
            NewsCountry.CANADA.code -> NewsCountry.CANADA
            else -> NewsCountry.USA
        }
    }
}