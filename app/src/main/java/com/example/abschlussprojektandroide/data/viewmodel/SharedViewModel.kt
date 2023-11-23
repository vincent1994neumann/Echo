package com.example.abschlussprojektandroide.data.viewmodel


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektandroide.data.dataclass.AppRepository
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application){
    private val repository = AppRepository()
    val quoteOfTheDay = repository.quoteOfTheDay
    val survey = repository.survey
    var firebaseAuth = FirebaseAuth.getInstance()




    //LiveData Überwachung vom User
    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser :LiveData<FirebaseUser?>
        get() = repository.firestore.currentUser

    //LiveData für die Abstimmungen der Surveys
    private val _voteResult = MutableLiveData<Result<String>>()
    val voteResult: LiveData<Result<String>> = _voteResult



    init {
        repository.loadSurveys()
    }


    fun updateSurveyItem(surveyItem: SurveyItem){
       repository.firestore.updateSurveyItem(surveyItem)
    }

// Firestore Login Logik Aufruf
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

    //QQD API Call
    fun loadQuoteOfTheDay(category: String? = null) {
        viewModelScope.launch {
                repository.loadQuoteOfTheDay()
        }
    }
}

