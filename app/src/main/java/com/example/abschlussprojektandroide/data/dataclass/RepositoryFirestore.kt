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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepositoryFirestore {

    var firebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    var TAG = "RepositoryFirestore"

    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser




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

    fun register(email: String, password: String, confirmPassword: String, username:String, context: Context) {
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
                    addSurveyToUser(it.uid,surveyItemId)
                }
                db.collection("SurveyItem")
                    .document(it.id).update(
                        mapOf("surveyId" to it.id)
                    )
            }
            .addOnFailureListener {
                Log.d("FirestorRepository", "Failed: $it")
            }
    }

    fun addSurveyToUser(userId:String,surveyId:String){
        val userRef = db.collection("users").document(userId)
        userRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.userCreatedSurveys?.add(surveyId)
            userRef.update("userCreatedSurveys",user?.userCreatedSurveys)
        }.addOnFailureListener { e ->
            Log.e(TAG, "Error adding survey to user: ", e)
        }
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