package com.digvijay.newsapp

import com.digvijay.newsapp.domain.NewsArticle
import com.digvijay.newsapp.framework.NewsApiDataSource
import com.digvijay.newsapp.framework.api.NewsApiService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class NewsApiDataSourceTest {

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApiService::class.java)

    private val sut = NewsApiDataSource(api)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should fetch news headlines correctly given 200 ok response`() {
        mockWebServer.enqueueResponse("headlines_response_200.json", 200)

        runBlocking {
            val actual = sut.getNewsHeadlines("us")[1]

            val expected =
                NewsArticle(
                    "Derrick Bryson Taylor",
                    "Severe Weather Helps Contain Florida Wildfires - The New York Times",
                    "The Chipola Complex, which encompasses three fires, has burned more than 34,000 acres in the Florida Panhandle. More severe weather is expected on Friday, meteorologists said.",
                    "https://static01.nyt.com/images/2022/03/09/multimedia/09xp-florida-wildfires-03/09xp-florida-wildfires-03-facebookJumbo.jpg",
                    "2022-03-10T10:07:30Z",
                    "A series of severe storms and substantial rainfall on Wednesday helped firefighters in the Florida Panhandle in their efforts to contain wildfires that are threatening nearby communities, officials s… [+740 chars]"
                )
            assertEquals(actual,expected)
        }
    }

    private fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")

        val source = inputStream?.let { inputStream.source().buffer() }
        source?.let {
            enqueue(
                MockResponse()
                    .setResponseCode(code)
                    .setBody(source.readString(StandardCharsets.UTF_8))
            )
        }
    }
}