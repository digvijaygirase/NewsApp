package com.digvijay.newsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.digvijay.newsapp.data.CountryRepository
import com.digvijay.newsapp.data.NewsRepository
import com.digvijay.newsapp.domain.NewsArticle
import com.digvijay.newsapp.framework.api.Resource
import com.digvijay.newsapp.framework.api.dto.HeadlinesResponse
import com.digvijay.newsapp.presentation.headlines.HeadlinesViewModel
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import okio.buffer
import okio.source
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.charset.StandardCharsets

@RunWith(AndroidJUnit4::class)
class HeadlinesViewModelTest {

    private lateinit var viewModel: HeadlinesViewModel

    @MockK
    private lateinit var observer: Observer<Resource<List<NewsArticle>>>

    @MockK
    private lateinit var newsRepository: NewsRepository

    @MockK
    private lateinit var countryRepository: CountryRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = HeadlinesViewModel(newsRepository, countryRepository)
    }

    @Test
    fun `verify livedata value changes on event`() {
        viewModel.headlinesResource.observeForever(observer)
        every { observer.onChanged(any()) } returns Unit
        every { countryRepository.getCountrySelection().code } returns ""
        coEvery { newsRepository.getHeadlines(any()) } returns ("headlines_response_200.json".getResponseModelFromJson())
        viewModel.fetchNewsHeadlines()
        Assert.assertNotNull(viewModel.headlinesResource.value)
        Assert.assertEquals(20, viewModel.headlinesResource.value?.data?.size)
        verifySequence {
            observer.onChanged(Resource.loading(null))
            observer.onChanged(
                Resource.success("headlines_response_200.json".getResponseModelFromJson())
            )
        }
    }

    private fun String.getResponseModelFromJson(): List<NewsArticle> {
        val inputStream =
            this@HeadlinesViewModelTest.javaClass.classLoader?.getResourceAsStream("api-response/${this}")
        val source = inputStream?.let { inputStream.source().buffer() }
        val newsArticlesList = mutableListOf<NewsArticle>()
        source?.let {
            val json = source.readString(StandardCharsets.UTF_8)
            val articlesList = Gson().fromJson(json, HeadlinesResponse::class.java).articles
            articlesList?.forEach {
                newsArticlesList.add(
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
        }
        return newsArticlesList
    }
}