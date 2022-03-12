package com.digvijay.newsapp.data

import com.digvijay.newsapp.domain.NewsArticle

interface NewsDataSource {

    suspend fun getNewsHeadlines(countryCode: String): List<NewsArticle>
}