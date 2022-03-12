package com.digvijay.newsapp.data

import com.digvijay.newsapp.domain.NewsCountry

class CountryRepository(private val countryDataSource: CountryDataSource) {

    fun getCountrySelection() = countryDataSource.getCountry()
    fun saveCountrySelection(newsCountry: NewsCountry) = countryDataSource.saveCountry(newsCountry)
}