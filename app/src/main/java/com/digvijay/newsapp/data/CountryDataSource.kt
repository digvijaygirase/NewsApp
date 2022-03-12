package com.digvijay.newsapp.data

import com.digvijay.newsapp.domain.NewsCountry

interface CountryDataSource {

    fun saveCountry(country: NewsCountry)
    fun getCountry(): NewsCountry
}