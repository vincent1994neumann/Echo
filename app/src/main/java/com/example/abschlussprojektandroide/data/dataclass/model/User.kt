package com.example.abschlussprojektandroide.data.dataclass.model

import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem

data class User(
    var userId: String, // Ein eindeutiger Identifikator für jeden Benutzer.
    var username: String,  // Der Benutzername des Benutzers.
    var profilePicture: String? = null, // Bild, das als Profilbild des Benutzers dient.
    var userCreatedSurveys: MutableList<String> = mutableListOf(), // Eine Liste von Survey-IDs, die der Benutzer erstellt hat.
    var savedSurveys: MutableList<String> = mutableListOf(), // Eine Liste an Survey Id, die der Benutzer gespeichert hat
    var idSurveyListe: MutableList<String> = mutableListOf(), //Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits abgestimmt hat.
    var idQuestionVoteList: MutableList<String> = mutableListOf() //Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits gevoted hat.
){
    constructor(userdata: Map<String,Any>) : this (
        userId = userdata["userId"] as String,
        username = userdata["username"] as String,
        profilePicture = userdata["profilPicture"] as String?,
        userCreatedSurveys = userdata["userCreatedSurveys"] as MutableList<String>,
        savedSurveys = userdata["savedSurveys"] as MutableList<String>,
        idSurveyListe = userdata["idSurveyListe"] as MutableList<String>,
        idQuestionVoteList = userdata["idQuestionVoteList"] as MutableList<String>
    )

}


