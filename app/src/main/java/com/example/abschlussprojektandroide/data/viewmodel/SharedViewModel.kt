package com.example.abschlussprojektandroide.data.viewmodel


import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektandroide.data.dataclass.AppRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SharedViewModel(application: Application) : AndroidViewModel(application){
    private val repository = AppRepository()
    val survey = repository.survey
    var firebaseAuth = FirebaseAuth.getInstance()


    //LiveData Überwachung vom User
    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser :LiveData<FirebaseUser?>
        get() = _currentUser




    init {
        repository.loadSurveys()
    }

// Firestore Login Logik
    fun login(email: String, password:String){
    try { firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // festlegen vom currentUser
                    _currentUser.value = firebaseAuth.currentUser
                }
    }

    }catch (e:Exception){
        Toast.makeText(getApplication(), "Login unsuccessful", Toast.LENGTH_LONG).show()
    }
    }

    fun register(email: String, password: String, confirmPassword: String){
    try { firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            _currentUser.value = firebaseAuth.currentUser
        }
    }
    }catch (e:Exception){
        Toast.makeText(getApplication(),"Registration unsuccessful",Toast.LENGTH_LONG).show()

    }    }

    fun logout(){
        _currentUser.value = null
        firebaseAuth.signOut()

    }
}