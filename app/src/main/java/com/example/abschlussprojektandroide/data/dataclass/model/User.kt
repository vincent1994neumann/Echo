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

    companion object{
        fun fromFireBase(snapshot: DocumentSnapshot):User{
            return User(
                snapshot["userId"] as String,
                snapshot["username"] as String,
                snapshot["userCreatedSurveys"] as MutableList<String>,
                snapshot["savedSurveys"] as MutableList<String>,
                snapshot["idSurveyList"] as MutableList<String>,
                snapshot["idQuestionVoteList"] as MutableList<String>,
            )
        }
    }

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

/*
    constructor(userdata: Map<String,Any>) : this (
        userId = userdata["userId"] as String,
        username = userdata["username"] as String,
        userCreatedSurveys = userdata["userCreatedSurveys"] as MutableList<String>,
        savedSurveys = userdata["savedSurveys"] as MutableList<String>,
        idSurveyList = userdata["idSurveyListe"] as MutableList<String>,
        idQuestionVoteList = userdata["idQuestionVoteList"] as MutableList<String>
    )


 */


}


