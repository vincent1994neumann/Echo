package com.example.abschlussprojektandroide.data.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojektandroide.data.dataclass.AppRepository
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel(){
    private val repository = AppRepository()
    val survey = repository.survey

    init {
        repository.loadSurveys()
    }



}