package com.example.androiddevchallenge

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

suspend fun searchDogs(): SearchResponse? {
    return try {
        NetService.create().searchPhotos(
            "dog", 1, 20
        )
    } catch (e: Exception) {
        null
    }
}

interface NetService {

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): SearchResponse?

    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"

        fun create(): NetService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NetService::class.java)
        }
    }
}

data class SearchResponse(
    val results: List<Dog>,
    val total_pages: Int
)