package com.example.abschlussprojektandroide.data.dataclass.model

import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.google.firebase.firestore.AggregateQuerySnapshot
import com.google.firebase.firestore.DocumentSnapshot

data class User(
    var userId: String= "", // Ein eindeutiger Identifikator für jeden Benutzer.
    var username: String="",  // Der Benutzername des Benutzers.
    var userCreatedSurveys: MutableList<String> = mutableListOf(), // Eine Liste von Survey-IDs, die der Benutzer erstellt hat.
    var savedSurveys: MutableList<String> = mutableListOf(), // Eine Liste an Survey Id, die der Benutzer gespeichert hat
    var idSurveyList: MutableList<String> = mutableListOf(), //Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits abgestimmt hat.
    var idQuestionVoteList: MutableList<String> = mutableListOf() //Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits gevoted hat.
){

    fun toFirebase():Map<String,Any>{
        return mapOf(
            "userId" to userId,
            "username" to username,
            "userCreatedSurveys" to userCreatedSurveys,
            "savedSurveys" to savedSurveys,
            "idSurveyList" to idSurveyList,
            "idQuestionVoteList" to idQuestionVoteList
        )
    }

}


