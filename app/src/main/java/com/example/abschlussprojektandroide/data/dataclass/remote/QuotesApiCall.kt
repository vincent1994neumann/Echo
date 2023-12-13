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

/* Moshi-Objekt für JSON-Serialisierung und Deserialisierung.
   Moshi wird mit einem KotlinJsonAdapterFactory konfiguriert, um Kotlin-spezifische Features zu unterstützen. */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/* OkHttpClient-Instanz mit einem Interceptor, der einen API-Schlüssel zu jedem Request hinzufügt.
   Dies ist notwendig, um die Authentifizierung bei der API zu gewährleisten. */
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .header("X-TheySaidSo-Api-Secret", "6pmdN5BBWu1xeT6ZDc8miXGYnBYmz6GnxNRihiD2")
            .build()
        chain.proceed(request)
    }
    .build()

/* Retrofit-Instanz, die den OkHttpClient und MoshiConverterFactory verwendet.
   Diese Instanz wird für die Kommunikation mit der REST-API verwendet. */
private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/* Interface für den API-Service.
   Hier wird eine Methode definiert, um das Zitat des Tages (Quote of the Day) abzurufen.
   Die Methode ist suspendiert, was bedeutet, dass sie in einer Coroutine aufgerufen werden kann. */
interface QuotesApiService {
    @GET("qod")
    suspend fun getQuoteOfTheDay(
        //@Query("category") category: String?,
        @Query("language") language: String? = "en"
    ): Response<QODResponse>
}

/* Objekt, das den Retrofit-Service für den API-Service bereitstellt.
   Dieses Objekt wird verwendet, um Instanzen des API-Services zu erstellen. */
    object QuotesApi{
        val retrofitService : QuotesApiService by lazy { retrofit.create(QuotesApiService::class.java) }
    }


