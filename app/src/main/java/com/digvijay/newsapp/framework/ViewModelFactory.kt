package com.digvijay.newsapp.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.digvijay.newsapp.data.CountryRepository
import com.digvijay.newsapp.data.NewsRepository
import com.digvijay.newsapp.framework.api.NewsApiService
import com.digvijay.newsapp.presentation.headlines.HeadlinesViewModel

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HeadlinesViewModel::class.java)) {
            HeadlinesViewModel(
                NewsRepository(NewsApiDataSource(NewsApiService())),
                CountryRepository(CountryPrefsDataSource(application))
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}