package com.example.abschlussprojektandroide.data.viewmodel


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektandroide.data.dataclass.AppRepository
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.dataclass.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

// Erweitert AndroidViewModel und nutzt die Application-Instanz für Kontext-spezifische Operationen
class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository() // Eine Instanz von AppRepository, das für die Datenabstraktion sorgt
    val quoteOfTheDay = repository.quoteOfTheDay // LiveData für das Zitat des Tages
    val survey = repository.survey // LiveData für Umfragen
    var firebaseAuth = FirebaseAuth.getInstance() // Instanz von FirebaseAuth für Authentifizierungszwecke


    // LiveData-Objekte für die Überwachung des aktuellen Firebase- und App-Benutzers
    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser :LiveData<FirebaseUser?>
        get() = repository.firestore.currentUser

    private val _currentAppUser = MutableLiveData<User?>()
    val currentAppUser :LiveData<User?>
        get()= _currentAppUser

    init {

    }

    // Holt aktuelle Benutzerdaten aus Firebase und aktualisiert _currentAppUser
    fun fetchCurrentUser() {
        // Zunächst holen wir die Benutzer-ID des aktuell angemeldeten Benutzers.
        val userId = firebaseAuth.currentUser?.uid
        // Überprüfen Sie, ob eine Benutzer-ID vorhanden ist, d.h., ob der Benutzer angemeldet ist.
        if (userId != null) {
            // Wenn ein Benutzer angemeldet ist, rufen wir sein Dokument aus der 'users'-Sammlung ab.
            repository.firestore.db.collection("users").document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    // Im Erfolgsfall versuchen wir, das Dokument in ein User-Objekt zu konvertieren.
                    val user = documentSnapshot.toObject(User::class.java)
                    // Wenn die Konvertierung erfolgreich ist, aktualisieren wir die LiveData _currentAppUser.
                    // LiveData benachrichtigt dann die Beobachter, in diesem Fall das UI (Fragment/Activity),
                    // dass sich die Daten geändert haben.
                    _currentAppUser.postValue(user)
                }.addOnFailureListener { e ->
                    // Im Falle eines Fehlers beim Datenabruf loggen wir die Fehlermeldung.
                    Log.e("SharedViewModel", "Error getting user: ", e)
                }
        }
    }

    fun updateSurveyItem(surveyItem: SurveyItem){
       repository.firestore.updateSurveyItem(surveyItem)
    }

// Firestore Login Logik Aufruf
    fun login(email: String, password:String,context: Context){
    repository.firestore.login(email,password, context)
    }
//Firestore Registrierung Logik Aufruf
    fun register(email: String, password: String, confirmPassword: String,username:String,context: Context){
    repository.firestore.register(email,password,confirmPassword,username,context)
    }

    fun logout(){
    repository.firestore.logout()
    }

    fun saveSurveyItem (surveyItem: SurveyItem){
        repository.firestore.saveSurveyItem(surveyItem)
    }

    // Funktionen zur Verwaltung von Benutzerfavoriten und Suchanfragen
    fun updateFavoriteSurveys(surveyId: String, shouldSave: Boolean) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        viewModelScope.launch {
            if (shouldSave) {
                repository.firestore.addSurveyToUserFavoriteList(userId, surveyId,shouldSave)
            } else {
                repository.firestore.removeSurveyFromUserFavoriteList(userId,surveyId)
            }
        }
    }

    //SearchAbfrage
    fun getFilteredSurveysForSearch(search: String): LiveData<List<SurveyItem>>{
        return repository.firestore.getFilteredSurveysForSearch(search)
    }

    //ProfilTabMenü

    fun getUserCreatedSurveys(userId: String): LiveData<List<SurveyItem>> {
        return repository.firestore.getUserSurvey(userId)
    }

    fun getUserSavedSurveys(userId: String): LiveData<List<SurveyItem>> {
        return repository.firestore.getUserSavedSurveys(userId)
    }

    //HomeTabMenü
    fun getLatestSurveysForHomepage():LiveData<List<SurveyItem>>{
        return repository.firestore.getLatestSurveysForHomepage()
    }
    fun getSurveysUserNotVoted():LiveData<List<SurveyItem>>{
        return repository.firestore.getSurveysUserNotVoted(currentAppUser.value?.userId!!)
    }
    fun getSurveysByQuestionCount():LiveData<List<SurveyItem>>{
        return repository.firestore.getSurveysByQuestionCount()
    }



    //QQD API Call
    // Ruft das Zitat des Tages von einer externen API ab
    fun loadQuoteOfTheDay(category: String? = null) {
        viewModelScope.launch {
                repository.loadQuoteOfTheDay()
        }
    }
}

