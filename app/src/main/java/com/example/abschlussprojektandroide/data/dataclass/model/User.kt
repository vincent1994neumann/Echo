package com.example.abschlussprojektandroide.data.dataclass.model

import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.google.firebase.firestore.AggregateQuerySnapshot
import com.google.firebase.firestore.DocumentSnapshot

/*
   Diese Datei definiert die Datenklasse 'User', welche für die Benutzerverwaltung innerhalb der Anwendung verwendet wird.
   Sie ist ein zentraler Bestandteil des Model-Teils im MVVM-Muster (Model-View-ViewModel), da sie die Datenstruktur für Benutzerobjekte bereitstellt.
   Die Einbeziehung von Firebase Firestore zeigt, dass diese Datenklasse für die Interaktion mit einer NoSQL-Datenbank ausgelegt ist.
*/

data class User(
    var userId: String= "", // Ein eindeutiger Identifikator für jeden Benutzer. Wichtig für die Identifizierung und Verwaltung von Benutzerkonten.
    var username: String="",  // Der Benutzername des Benutzers. Dient als menschenlesbare Identifikation des Benutzers in der Anwendung.
    var userCreatedSurveys: MutableList<String> = mutableListOf(), // Eine Liste von Survey-IDs, die der Benutzer erstellt hat. Unterstützt die Funktionen zur Verwaltung eigener Umfragen.
    var savedSurveys: MutableList<String> = mutableListOf(), // Eine Liste an Survey-IDs, die der Benutzer gespeichert hat. Ermöglicht es Benutzern, Umfragen zur späteren Ansicht zu speichern.
    var idSurveyList: MutableList<String> = mutableListOf(), //Eine Liste oder ein Set von IDs der Umfragen, bei denen der Benutzer bereits abgestimmt hat. Verhindert Mehrfachabstimmungen.
    var idQuestionVoteList: MutableList<String> = mutableListOf() //Eine Liste oder ein Set von IDs der Fragen, bei denen der Benutzer bereits abgestimmt hat. Unterstützt die Logik zur Verhinderung von Mehrfachabstimmungen.
){
    /*
       Die Methode 'toFirebase()' konvertiert die User-Daten in ein Map-Format, welches für die Speicherung und Verwaltung in Firebase Firestore geeignet ist.
       Diese Methode ist ein Teil der Integration der Datenklasse mit der Firebase-Datenbank und spiegelt das Zusammenspiel zwischen dem Model und externen Datenquellen wider.
    */
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



