package com.digvijay.newsapp.framework

import android.util.Log
import com.digvijay.newsapp.data.NewsDataSource
import com.digvijay.newsapp.domain.NewsArticle
import com.digvijay.newsapp.framework.api.NewsApiService
import com.digvijay.newsapp.framework.api.Status

const val TAG = "NewsApiNewsDataSource"

class NewsApiDataSource(private val newsApiService: NewsApiService) : NewsDataSource {

    override suspend fun getNewsHeadlines(countryCode: String): List<NewsArticle> {
        val headlinesResponse = newsApiService.getHeadlines(countryCode)
        val headlines = mutableListOf<NewsArticle>()
        if (headlinesResponse.isSuccessful) {
            headlinesResponse.body()?.run {
                if(status == Status.SUCCESS.code){
                    articles?.forEach {
                        headlines.add(
                            NewsArticle(
                                it.author,
                                it.title,
                                it.description,
                                it.urlToImage,
                                it.publishedAt,
                                it.content
                            )
                        )
                    }
                }else{
                    Log.e(TAG, "error code: $code, message: $message")
                }
            }
        }
        return headlines
    }
}