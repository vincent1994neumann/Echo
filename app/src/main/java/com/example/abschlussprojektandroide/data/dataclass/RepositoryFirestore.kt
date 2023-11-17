package com.example.abschlussprojektandroide.data.dataclass

import android.content.Context

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepositoryFirestore{

    var firebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser : LiveData<FirebaseUser?>
        get() = _currentUser

    fun login(email: String, password:String,context: Context){
        try { firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("RepositoryFirestore", "Login erfolgreich für Benutzer: ${email} Repo")
                // festlegen vom currentUser
                _currentUser.value = firebaseAuth.currentUser
            }
        }

        }catch (e:Exception){
            Log.d("RepositoryFirestore", "Login nicht erfolgreich für Benutzer: ${email}")
        }
    }

    fun register(email: String, password: String, confirmPassword: String,context: Context){
        try { firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _currentUser.value = firebaseAuth.currentUser
                Log.d("RepositoryFirestore", "Registrierung erfolgreich für Benutzer: ${email}")
            }
        }
        }catch (e:Exception){
            Log.d("RepositoryFirestore", "Registrierung nicht erfolgreich für Benutzer: ${email}")
            Toast.makeText(context,"Registration unsuccessful",Toast.LENGTH_LONG).show()

        }
    }
    fun logout(){
        _currentUser.value = null
        firebaseAuth.signOut()
    }


    fun saveSurveyItem(surveyItem: SurveyItem){
        db.collection("SurveyItem")
            .add(surveyItem.toMap())
            .addOnSuccessListener {
                Log.d("FirestorRepository", "DocumentSnapshot added with ID: ${it.id}")
                    db.collection("SurveyItem")
                        .document(it.id).update(
                            mapOf("surveyId" to it.id)
                        )
            }
            .addOnFailureListener {
                Log.d("FirestorRepository", "Failed: $it")
            }

    }
}