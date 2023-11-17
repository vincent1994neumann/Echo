package com.example.abschlussprojektandroide.data.dataclass.remote

import com.example.abschlussprojektandroide.data.dataclass.model.api.QODResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL= "https://quotes.rest/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


// OkHttpClient mit API-Schlüssel Interceptor
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .header("X-TheySaidSo-Api-Secret", "6pmdN5BBWu1xeT6ZDc8miXGYnBYmz6GnxNRihiD2")
            .build()
        chain.proceed(request)
    }
    .build()

// Retrofit Instanz mit OkHttpClient
private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface QuotesApiService {
    @GET("qod")
    suspend fun getQuoteOfTheDay(
        //@Query("category") category: String?,
        @Query("language") language: String? = "en"
    ): Response<QODResponse>
}


    object QuotesApi{
        val retrofitService : QuotesApiService by lazy { retrofit.create(QuotesApiService::class.java) }
    }


