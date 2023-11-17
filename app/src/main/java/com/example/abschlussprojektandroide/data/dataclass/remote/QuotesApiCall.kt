package com.example.abschlussprojektandroide.data.dataclass.remote

import com.example.abschlussprojektandroide.data.dataclass.model.QODResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Base64

const val BASE_URL= "https://quotes.rest/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface QuotesApiService {
    @GET("/qod")
    suspend fun getQuoteOfTheDay(
        @Query("category") category: String?,
        @Query("language") language: String? = "en"
    ): Response<QODResponse>
}


    object QuotesApi{
        val retrofitService : QuotesApiService by lazy { retrofit.create(QuotesApiService::class.java) }
    }
