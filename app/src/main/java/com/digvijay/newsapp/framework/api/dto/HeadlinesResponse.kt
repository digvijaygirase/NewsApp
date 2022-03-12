package com.digvijay.newsapp.framework.api.dto

data class HeadlinesResponse(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>?,
    val code: String?,
    val message: String?
)