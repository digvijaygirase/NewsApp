package com.digvijay.newsapp.domain

data class NewsArticle(
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val url:String?
)