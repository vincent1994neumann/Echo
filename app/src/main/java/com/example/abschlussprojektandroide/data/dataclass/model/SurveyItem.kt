package com.example.abschlussprojektandroide.data.dataclass.model


import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


data class SurveyItem(
    val surveyid: String, // Eindeutige ID, z.B. von einer Datenbank
    val userId: String,
    val timestamp:Timestamp, // Zeitstempel des Surveys
    val publishedBy: String, // Name des Veröffentlichers via ID
    val isPublished: Boolean = false, // Status der Umfrage: Veröffentlicht oder nicht veröffentlicht.
    val header: String, // Überschrift des Surveys
    val category: String, // Kategorie des Surveys (z.B. "Umwelt")
    val surveyText: String, // Text der eigentlichen Umfrage

    var surveySaved: Boolean = false, // Für den SaveBtn im Survey

    var totalVotes: Int = 0, // Gesamtanzahl der Stimmen
    var votesTrue: Int = 0, // Anzahl der "true" Stimmen
    var votesNeutral: Int = 0, // Anzahl der "neutral" Stimmen
    var votesFalse: Int = 0, // Anzahl der "false" Stimmen
    var hasVoted:Boolean = false,
    val votedUser: MutableSet<String> = mutableSetOf(), // Abfragen ob USer bereits abgestimmt hat, Set von User-IDs, die bereits abgestimmt haben
    var showPercentage: Boolean = false,

    var questionUpVotes: Int = 0, // Anzahl der Zustimmungen für die Umfrage (für das Ranking)
    var questionDownVotes: Int = 0, // Anzahl der Ablehnungen für die Umfrage (für das Ranking)
    var hasVotedQuestion: Boolean =false,


    ){
    fun percentageTrue(): String {
        return if (totalVotes > 0) {
            val trueVotePercentage = (votesTrue.toDouble() / totalVotes.toDouble()) * 100
            String.format("%.2f", trueVotePercentage) + "%"
        } else "0.00%"
    }

    fun percentageNeutral(): String {
        return if (totalVotes > 0) {
            val neutralVotePercentage = (votesNeutral.toDouble() / totalVotes.toDouble()) * 100
            String.format("%.2f", neutralVotePercentage) + "%"
        } else "0.00%"
    }


    fun percentageFalse(): String {
        return if (totalVotes > 0) {
            val falseVotePercentage = (votesFalse.toDouble() / totalVotes.toDouble()) * 100
            String.format("%.2f", falseVotePercentage) + "%"
        } else "0.00%"
    }

    //Logik zum Abstimmen der Frage


    // Enumeration der möglichen Abstimmungstypen
    enum class VoteType {
        TRUE, NEUTRAL, FALSE
    }
    // Funktion zum Hinzufügen einer Stimme zu einer Abstimmung
    fun addVote(userId: String, vote: VoteType): Boolean {
        // Überprüfen, ob der Benutzer bereits abgestimmt hat
        if (userId !in votedUser) {
            // Hinzufügen einer Stimme basierend auf dem Stimmentyp
            when (vote) {
                VoteType.TRUE -> votesTrue++     // Erhöhung der TRUE-Stimmen
                VoteType.NEUTRAL -> votesNeutral++ // Erhöhung der NEUTRAL-Stimmen
                VoteType.FALSE -> votesFalse++   // Erhöhung der FALSE-Stimmen
            }
            totalVotes++   // Gesamtanzahl der Stimmen erhöhen
            votedUser.add(userId)  // Benutzer zur Liste der abgestimmten Benutzer hinzufügen
            return true   // Rückgabe von 'true', da die Stimme erfolgreich hinzugefügt wurde
        }
        // Rückgabe von 'false', falls der Benutzer bereits abgestimmt hat
        return false
    }

    fun setVotingResults(trueVotes: Int, neutralVotes: Int, falseVotes: Int) {
        votesTrue = trueVotes
        votesNeutral = neutralVotes
        votesFalse = falseVotes
        totalVotes = trueVotes + neutralVotes + falseVotes
    }


    fun totalUpDownVotes ():String{
        var totalVotesQuestion= questionUpVotes-questionDownVotes
        return totalVotesQuestion.toString()
    }

    fun getFormattedTime(): String {
        val millis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val date = java.util.Date(millis)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

}



//Listen sind problematisch bei der ROOM-Datenbank ggf. Anpassungen erforderlich
