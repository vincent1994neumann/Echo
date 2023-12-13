package com.example.abschlussprojektandroide.data.dataclass

import android.content.Context

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.dataclass.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
// Klasse RepositoryFirestore, die als Schnittstelle zu Firebase Firestore und Firebase Auth fungiert.
class RepositoryFirestore {
    // Firebase Auth und Firestore Instanzen.
    var firebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    var TAG = "RepositoryFirestore"

    // MutableLiveData für den aktuellen Benutzer.
    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser


    // Funktionen für Login, Registrierung, Logout und Benutzerdaten-Management.
    fun login(email: String, password: String, context: Context) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RepositoryFirestore", "Login erfolgreich für Benutzer: ${email} Repo")
                    // festlegen vom currentUser
                    _currentUser.value = firebaseAuth.currentUser
                }
            }
        } catch (e: Exception) {
            Log.d("RepositoryFirestore", "Login nicht erfolgreich für Benutzer: ${email}")
        }
    }

    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        username: String,
        context: Context
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = User(
                    it.user?.uid!!,
                    username,
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                )
                db.collection("users")
                    .document(it.user?.uid!!)
                    .set(user.toFirebase())
                _currentUser.value = it.user
                Log.d("RepositoryFirestore", "Registrierung erfolgreich für Benutzer: ${email}")
            }
            .addOnFailureListener {
                Log.d(
                    "RepositoryFirestore",
                    "Registrierung nicht erfolgreich für Benutzer: ${email}"
                )
                Toast.makeText(context, "Registration unsuccessful", Toast.LENGTH_LONG).show()
            }
    }

    fun logout() {
        _currentUser.value = null
        firebaseAuth.signOut()
    }


    //________________________________________________________________________

    // Funktionen zur Verwaltung von SurveyItem-Daten in Firestore.
    fun updateSurveyItem(surveyItem: SurveyItem) {
        val surveyRef = db.collection("SurveyItem").document(surveyItem.surveyid)
        try {
            surveyRef.update(surveyItem.toMap())
        } catch (e: Exception) {
            Log.e(TAG, "Update from SurveyItem failed: $e")
        }
    }


    fun saveSurveyItem(surveyItem: SurveyItem) {
        db.collection("SurveyItem")
            .add(surveyItem.toMap())
            .addOnSuccessListener {
                Log.d("FirestorRepository", "DocumentSnapshot added with ID: ${it.id}")
                val surveyItemId = it.id
                val currentUser = firebaseAuth.currentUser
                currentUser?.let {
                    addSurveyToUser(it.uid, surveyItemId)
                }
                db.collection("SurveyItem")
                    .document(it.id).update(
                        mapOf("surveyid" to it.id)
                    )
            }
            .addOnFailureListener {
                Log.d("FirestorRepository", "Failed: $it")
            }
    }

    fun addSurveyToUser(userId: String, surveyId: String) {
        val userRef = db.collection("users").document(userId)
        userRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.userCreatedSurveys?.add(surveyId)
            userRef.update("userCreatedSurveys", user?.userCreatedSurveys)
        }.addOnFailureListener { e ->
            Log.e(TAG, "Error adding survey to user: ", e)
        }
    }


    //HomeTabMenü- Daten
    fun getLatestSurveysForHomepage(): LiveData<List<SurveyItem>> {
        val latestSurveysLiveData = MutableLiveData<List<SurveyItem>>()

        db.collection("SurveyItem")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val surveys = querySnapshot.toObjects(SurveyItem::class.java)
                latestSurveysLiveData.postValue(surveys)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting latest surveys: ", exception)
                latestSurveysLiveData.postValue(emptyList()) // Post an empty list or handle the error as needed
            }
        return latestSurveysLiveData
    }

    fun getSurveysUserNotVoted(userId: String): LiveData<List<SurveyItem>> {
        val notVotedSurveysLiveData = MutableLiveData<List<SurveyItem>>()

        db.collection("SurveyItem")
            // Annahme: Sie haben eine separate Feld "votedUserIds" mit einer Liste von User-IDs
            .whereNotIn("votedUser", listOf(userId))
            .get()
            .addOnSuccessListener { querySnapshot ->
                val surveys = querySnapshot.toObjects(SurveyItem::class.java)
                notVotedSurveysLiveData.postValue(surveys.filter { userId !in it.votedUser })
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting surveys user not voted: ", exception)
                notVotedSurveysLiveData.postValue(emptyList()) // Post an empty list or handle the error as needed
            }
        return notVotedSurveysLiveData
    }

    fun getSurveysByQuestionCount(): LiveData<List<SurveyItem>> {
        val surveysByVotesLiveData = MutableLiveData<List<SurveyItem>>()

        db.collection("SurveyItem")
            .orderBy("votedQuestionResult", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val surveys = querySnapshot.toObjects(SurveyItem::class.java)
                surveysByVotesLiveData.postValue(surveys)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting surveys by vote count: ", exception)
                surveysByVotesLiveData.postValue(emptyList()) // Post an empty list or handle the error as needed
            }
        return surveysByVotesLiveData
    }


    //Searchabfrage - alle Surveys werden geladen und dann gefiltert (nicht optimal da es bei großen Datenmenegen  ineffizient ist und zur Überschreitung der Abfrage Daten kommen kann)
    fun getFilteredSurveysForSearch(search: String): LiveData<List<SurveyItem>> {
        val searchResultsLiveData = MutableLiveData<List<SurveyItem>>()
        db.collection("SurveyItem")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val allSurveys = querySnapshot.toObjects(SurveyItem::class.java)
                val filteredSurveys = allSurveys.filter {
                    it.header.contains(search) ||
                            it.surveyText.contains(search) ||
                            it.category.contains(search)
                }
                searchResultsLiveData.postValue(filteredSurveys)
            }
            .addOnFailureListener { exception ->
                Log.e("Repository", "Error filtering surveys: ", exception)
                searchResultsLiveData.postValue(emptyList())
            }
        return searchResultsLiveData

    }


    fun getUserSurvey(userId: String): LiveData<List<SurveyItem>> {
        val userSurveysLiveData = MutableLiveData<List<SurveyItem>>()
        db.collection("users").document(userId).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (user != null && user.userCreatedSurveys.isNotEmpty()) {
                db.collection("SurveyItem")
                    .whereIn("surveyid", user.userCreatedSurveys)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val surveys = querySnapshot.toObjects(SurveyItem::class.java)
                        userSurveysLiveData.postValue(surveys)
                    }
            } else {
                userSurveysLiveData.postValue(emptyList())
            }
        }
        return userSurveysLiveData
    }

    fun getUserSavedSurveys(userId: String): LiveData<List<SurveyItem>> {
        // Initialisiere LiveData für die gespeicherten Umfragen.
        val savedSurveysLiveData = MutableLiveData<List<SurveyItem>>()

        // Abrufen von Benutzerdaten aus der Datenbank anhand der Benutzer-ID.
        db.collection("users").document(userId).get().addOnSuccessListener { documentSnapshot ->
            // Konvertiere das Dokumentensnapshot in ein User-Objekt.
            val user = documentSnapshot.toObject(User::class.java)

            // Überprüfe, ob Benutzerobjekt nicht null ist und gespeicherte Umfragen enthält.
            if (user != null && user.savedSurveys.isNotEmpty()) {
                // Abrufen der gespeicherten Umfragen, falls vorhanden.
                db.collection("SurveyItem")
                    .whereIn("surveyid", user.savedSurveys)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        // Konvertiere die erhaltenen Daten in SurveyItem-Objekte.
                        val surveys = querySnapshot.toObjects(SurveyItem::class.java)
                        // Aktualisiere LiveData mit den erhaltenen Umfragen.
                        savedSurveysLiveData.postValue(surveys)
                    }
            } else {
                // Wenn die Liste leer ist, geben Sie eine leere Liste zurück.
                // Gute Behandlung von Fällen, in denen keine Umfragen gespeichert sind.
                savedSurveysLiveData.postValue(emptyList())
            }
        }
        // Rückgabe der LiveData, die die gespeicherten Umfragen repräsentiert.
        return savedSurveysLiveData
    }

    fun addSurveyToUserFavoriteList(userId: String, surveyId: String, shouldSave: Boolean) {
        val userRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val userSnapshot = transaction.get(userRef)
            val user = userSnapshot.toObject(User::class.java)
            user?.let {
                if (shouldSave) {
                    if (!user.savedSurveys.contains(surveyId)) {
                        user.savedSurveys.add(surveyId)
                    }
                } else {
                    user.savedSurveys.remove(surveyId)
                }
                transaction.set(userRef, user)
            }
        }.addOnSuccessListener {
            Log.d(TAG, "Saved surveys updated successfully")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Error updating saved surveys: ", e)
            // Optional: Benutzerfeedback über UI-Komponenten
        }
    }

    fun removeSurveyFromUserFavoriteList(userId: String, surveyId: String) {
        val userRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val userSnapshot = transaction.get(userRef)
            val user = userSnapshot.toObject(User::class.java)
            user?.let {
                if (user.savedSurveys.contains(surveyId)) {
                    user.savedSurveys.remove(surveyId)
                    transaction.set(userRef, user)
                }
            }
        }.addOnSuccessListener {
            Log.d(TAG, "Survey removed from saved list successfully")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Error removing survey from saved list: ", e)
            // Optional: Benutzerfeedback über UI-Komponenten
        }
    }
}