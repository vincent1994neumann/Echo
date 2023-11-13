package com.example.abschlussprojektandroide.data.dataclass.model

import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem

data class User(
    var userId: String, // Ein eindeutiger Identifikator für jeden Benutzer.
    var username: String,  // Der Benutzername des Benutzers.
    var profilePicture: String? = null, // Bild, das als Profilbild des Benutzers dient.
    var userCreatedSurveys: MutableList<Int> = mutableListOf(), // Eine Liste von Survey-IDs, die der Benutzer erstellt hat.
    var savedSurveys: MutableList<Int> = mutableListOf(), // Eine Liste an Survey Id, die der Benutzer gespeichert hat
    var idSurveyListe: MutableList<Int> = mutableListOf(), //Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits abgestimmt hat.
    var idQuestionVoteList: MutableList<Int> = mutableListOf() //Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits gevoted hat.
){
    constructor(userdata: Map<String,Any>) : this (
        userId = userdata["userId"] as String,
        username = userdata["username"] as String,
        profilePicture = userdata["profilPicture"] as String?,
        userCreatedSurveys = userdata["userCreatedSurveys"] as MutableList<Int>,
        savedSurveys = userdata["savedSurveys"] as MutableList<Int>,
        idSurveyListe = userdata["idSurveyListe"] as MutableList<Int>,
        idQuestionVoteList = userdata["idQuestionVoteList"] as MutableList<Int>
    )

}


