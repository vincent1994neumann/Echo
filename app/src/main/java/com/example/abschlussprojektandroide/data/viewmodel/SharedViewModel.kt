package com.example.abschlussprojektandroide.data.viewmodel


import androidx.lifecycle.ViewModel
import com.example.abschlussprojektandroide.data.model.AppRepository

class SharedViewModel : ViewModel(){
    private val repository = AppRepository()
    val survey = repository.survey

    init {
        repository.loadSurveys()
    }

}