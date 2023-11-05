package com.example.abschlussprojektandroide.data.datamodels

data class User(
    val userId: Int, // Ein eindeutiger Identifikator für jeden Benutzer.
    val username: String,  // Der Benutzername des Benutzers.
    val email: String,  // Die E-Mail-Adresse des Benutzers.
    val password: String, // E Passworts des Benutzers.
    val profilePicture: String? = null, // Bild, das als Profilbild des Benutzers dient.
    val votes: MutableList<String> = mutableListOf(),  // Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits abgestimmt hat.
    val userCreatedSurveys: MutableList<SurveyItem> = mutableListOf(), // Eine Liste von Survey-IDs, die der Benutzer erstellt hat.
    val savedSurveys: MutableList<SurveyItem> = mutableListOf(),
    val idSurveyListe: MutableList<Int> = mutableListOf(),
    val idQuestionVoteList: MutableList<Int> = mutableListOf()
)
