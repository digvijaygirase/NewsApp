package com.digvijay.newsapp.presentation.headlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digvijay.newsapp.data.CountryRepository
import com.digvijay.newsapp.data.NewsRepository
import com.digvijay.newsapp.domain.NewsArticle
import com.digvijay.newsapp.domain.NewsCountry
import com.digvijay.newsapp.framework.api.Resource
import kotlinx.coroutines.launch

class HeadlinesViewModel(
    private val newsRepository: NewsRepository,
    private val countryRepository: CountryRepository
) : ViewModel() {

    private var _headlinesResource = MutableLiveData<Resource<List<NewsArticle>>>()
    val headlinesResource: LiveData<Resource<List<NewsArticle>>>
        get() = _headlinesResource

    fun fetchNewsHeadlines() {
        _headlinesResource.postValue(Resource.loading(null))
        viewModelScope.launch {
            val headlines =
                newsRepository.getHeadlines(countryRepository.getCountrySelection().code)
            if (headlines.isNotEmpty()) {
                _headlinesResource.postValue(Resource.success(headlines))
            } else {
                _headlinesResource.postValue(Resource.error(null, "Something went wrong."))
            }
        }
    }

    fun changeNewsCountry(country: NewsCountry) {
        countryRepository.saveCountrySelection(country)
    }

    fun getCurrentSelectedCountry() = countryRepository.getCountrySelection()
}