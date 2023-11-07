package com.example.abschlussprojektandroide.data.dataclass.model

import android.os.IBinder

data class SurveyItem(
    val surveyid: Int, // Eindeutige ID, z.B. von einer Datenbank
    val userId: Int = 0,
    val timestamp: String, // Zeitstempel des Surveys
    val publishedBy: String, // Name des Veröffentlichers via ID
    val isPublished: Boolean = false, // Status der Umfrage: Veröffentlicht oder nicht veröffentlicht.
    val header: String, // Überschrift des Surveys
    val category: String, // Kategorie des Surveys (z.B. "Umwelt")
    val surveyText: String, // Text der eigentlichen Umfrage
    val totalVotes: Int = 0, // Gesamtanzahl der Stimmen
    val votesTrue: Int = 0, // Anzahl der "true" Stimmen
    val votesNeutral: Int = 0, // Anzahl der "neutral" Stimmen
    val votesFalse: Int = 0, // Anzahl der "false" Stimmen
    val questionUpvotes: Int = 0, // Anzahl der Zustimmungen für die Umfrage (für das Ranking)
    val questionDownvotes: Int = 0, // Anzahl der Ablehnungen für die Umfrage (für das Ranking)
    val votedUser: MutableSet<Int> = mutableSetOf(), // Abfragen ob USer bereits abgestimmt hat, Set von User-IDs, die bereits abgestimmt haben
    )

//Listen sind problematisch bei der ROOMDatenbank ggf. Anpassungen erforderlich
