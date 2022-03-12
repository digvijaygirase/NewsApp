package com.digvijay.newsapp.data

class NewsRepository(private val newsDataSource: NewsDataSource) {

    suspend fun getHeadlines(countryCode: String) = newsDataSource.getNewsHeadlines(countryCode)
}