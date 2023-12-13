package com.example.abschlussprojektandroide.data.dataclass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektandroide.data.dataclass.model.api.Quote
import com.example.abschlussprojektandroide.data.dataclass.remote.QuotesApi
import kotlinx.coroutines.launch
// Klasse ApiRepository, welche als Vermittler zwischen der API und der UI fungiert.
class ApiRepository {


    // MutableLiveData für den QuoteOfDay. Diese wird von der UI beobachtet.
    private val _quoteOfTheDay = MutableLiveData<Quote>()
    // Öffentlich zugängliches LiveData, das von der UI beobachtet wird.
    val quoteOfTheDay: LiveData<Quote> = _quoteOfTheDay

    // Funktion zum Laden des Zitats des Tages von der API.
    suspend fun loadQuoteOfTheDay(category: String? = null) {
        try {
            val response = QuotesApi.retrofitService.getQuoteOfTheDay()
            if (response.isSuccessful && response.body() != null) {
                _quoteOfTheDay.postValue(response.body()!!.contents.quotes.first())
                Log.d(
                    "ApiRepository",
                    "Zitat des Tages geladen: ${response.body()!!.contents.quotes.first().quote}"
                )
            } else {
                Log.e("ApiRepository", "API-Antwort war nicht erfolgreich: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("ApiRepository", "Fehler beim Laden des Zitats des Tages... $e")
        }
    }
}


