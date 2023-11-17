package com.example.abschlussprojektandroide.data.viewmodel


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektandroide.data.dataclass.AppRepository
import com.example.abschlussprojektandroide.data.dataclass.model.Quote
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.dataclass.remote.QuotesApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application){
    private val repository = AppRepository()
    val survey = repository.survey
    var firebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore



    //LiveData Überwachung vom User
    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser :LiveData<FirebaseUser?>
        get() = repository.firestore.currentUser

    //LiveData für die Abstimmungen der Surveys
    private val _voteResult = MutableLiveData<Result<String>>()
    val voteResult: LiveData<Result<String>> = _voteResult

    //LiveData für den QuoteOfDay
    private val _quoteOfTheDay = MutableLiveData<Quote>()
    val quoteOfTheDay: LiveData<Quote> = _quoteOfTheDay

    init {
        repository.loadSurveys()
    }

// Firestore Login Logik
    fun login(email: String, password:String,context: Context){
    repository.firestore.login(email,password, context)
    }

    fun register(email: String, password: String, confirmPassword: String,context: Context){
    repository.firestore.register(email,password,confirmPassword,context)
    }

    fun logout(){
    repository.firestore.logout()
    }

    fun saveSurveyItem (surveyItem: SurveyItem){
        repository.firestore.saveSurveyItem(surveyItem)
    }



    fun addVoteToSurvey(surveyId:String, userId:String, voteType: SurveyItem.VoteType){
        val db = FirebaseFirestore.getInstance()
        val surveyRef = db.collection("SurveyItem").document(surveyId)

        db.runTransaction{transaction ->
            val snapshot = transaction.get(surveyRef)
            val surveyItem = snapshot.toObject(SurveyItem::class.java)
            surveyItem?.let {
                if (it.addVote(userId,voteType)){
                    val votedUserList = it.votedUser.toList()
                    transaction.update(surveyRef,"votesOption1", it.votesOption1)
                    transaction.update(surveyRef,"votesOption2",it.votesOption2)
                    transaction.update(surveyRef, "votesOption3", it.votesOption3)
                    transaction.update(surveyRef,"votesOption4",it.votesOption4)
                    transaction.update(surveyRef, "totalVotes", it.totalVotes)
                    transaction.update(surveyRef, "votedUser", votedUserList)
                }else{
                    // Wenn der Benutzer bereits abgestimmt hat, tun Sie nichts oder werfen eine Exception
                    throw FirebaseFirestoreException(
                        "User has already voted",
                        FirebaseFirestoreException.Code.ABORTED)
                }
            }

        }.addOnSuccessListener {
            _voteResult.value = Result.success("Vote added successfully.")
        }.addOnFailureListener{
            _voteResult.value = Result.failure(exception = Throwable("Voting Failed"))
        }
    }



    //QQD API Call
    fun loadQuoteOfTheDay(category: String? = null) {
        viewModelScope.launch {
            try {
                val response = QuotesApi.retrofitService.getQuoteOfTheDay(category)
                if (response.isSuccessful && response.body() != null) {
                    // Verarbeite die Antwort
                    val quoteOfTheDay = response.body()!!.contents.quotes.first()
                    Log.d("SharedViewModel", "Zitat des Tages geladen: ${_quoteOfTheDay.value?.quote}")
                } else {
                    Log.e("SharedViewModel", "API-Antwort war nicht erfolgreich oder leer.")
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Fehler beim Laden des Zitats des Tages", e)
            }
        }
    }
}