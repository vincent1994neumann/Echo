package com.example.abschlussprojektandroide.data.dataclass.model

import com.example.abschlussprojektandroide.util.VoteType
import com.example.abschlussprojektandroide.util.VoteTypeQuestion
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

/* Das folgende Kotlin-Data-Class-Modell `SurveyItem` repräsentiert ein Umfrageelement in einer Android-Anwendung.
   Es wird das MVVM-Designmuster genutzt, wobei diese Klasse als Teil des 'Model'-Aspekts dient. Dieses Modell speichert
   alle relevanten Daten einer Umfrage, wie z.B. ID, Nutzerinformationen, Zeitstempel und Umfragedetails. */

data class SurveyItem(
    var surveyid: String = "", // Eindeutige ID, z.B. von einer Datenbank
    var userId: String = "",
    var timestamp: Timestamp = Timestamp.now(), // Zeitstempel des Surveys
    var publishedBy: String = "", // Name des Veröffentlichers via ID
    var isPublished: Boolean = false, // Status der Umfrage: Veröffentlicht oder nicht veröffentlicht.
    var header: String = "", // Überschrift des Surveys
    var category: String = "", // Kategorie des Surveys (z.B. "Umwelt")
    var surveyText: String = "", // Text der eigentlichen Umfrage
    var answerOption1: String = "",
    var answerOption2: String = "",
    var answerOption3: String = "",
    var answerOption4: String = "",
    var surveySaved: Boolean = false, // Für den SaveBtn im Survey
    var totalVotes: Int = 0, // Gesamtanzahl der Stimmen
    var votesOption1: Int = 0, // Anzahl der "1" Stimmen
    var votesOption2: Int = 0, // Anzahl der "2" Stimmen
    var votesOption3: Int = 0, // Anzahl der "3" Stimmen
    var votesOption4: Int = 0,// Anzahl der "4" Stimmen
    var selectedOption: VoteType? = null, // Standardmäßig ist keine Option ausgewählt
    var hasVoted: Boolean = false,
    var votedUser: MutableList<String> = mutableListOf(), // Abfragen ob User bereits abgestimmt hat, Set von User-IDs, die bereits abgestimmt haben
    var showPercentage: Boolean = false,
    var votedQuestionResult: Int = 0,
    var votedQuestionUser: MutableList<String> = mutableListOf(),
    var questionUpVotes: Int = 0, // Anzahl der Zustimmungen für die Umfrage (für das Ranking)
    var questionDownVotes: Int = 0, // Anzahl der Ablehnungen für die Umfrage (für das Ranking)
    var hasVotedQuestion: Boolean = false,


    ) {
    //Ergebniss Berechnung
    /* Die Funktionen `percentageOption1` bis `percentageOption4` berechnen den Prozentsatz der Stimmen für jede Antwortoption.
       Sie verwenden eine bedingte Logik, um zu verhindern, dass eine Division durch Null auftritt, wenn keine Stimmen abgegeben wurden. */

    fun percentageOption1(): String {
        return if (totalVotes > 0) {
            val option1VotePercentage = (votesOption1.toDouble() / totalVotes.toDouble()) * 100
            String.format("%.2f", option1VotePercentage) + "%"
        } else "0.00%"
    }

    fun percentageOption2(): String {
        return if (totalVotes > 0) {
            val option2VotePercentage = (votesOption2.toDouble() / totalVotes.toDouble()) * 100
            String.format("%.2f", option2VotePercentage) + "%"
        } else "0.00%"
    }

    fun percentageOption3(): String {
        return if (totalVotes > 0) {
            val option3VotePercentage = (votesOption3.toDouble() / totalVotes.toDouble()) * 100
            String.format("%.2f", option3VotePercentage) + "%"
        } else "0.00%"
    }

    fun percentageOption4(): String {
        return if (totalVotes > 0) {
            val option4VotePercentage = (votesOption4.toDouble() / totalVotes.toDouble()) * 100
            String.format("%.2f", option4VotePercentage) + "%"
        } else "0.00%"
    }

    //Logik zum Abstimmen der Frage
    /* Die `addVote` Methode ermöglicht es, Stimmen für eine Umfrageoption hinzuzufügen.
       Diese Methode prüft zunächst, ob der Benutzer bereits abgestimmt hat, um Mehrfachabstimmungen zu vermeiden.
       Sie erhöht die entsprechende Stimmenzahl und aktualisiert die Gesamtanzahl der Stimmen. */

    // Enumeration der möglichen Abstimmungstypen
    // Funktion zum Hinzufügen einer Stimme zu einer Abstimmung
    fun addVote(userId: String, vote: VoteType): Boolean {
        // Überprüfen, ob der Benutzer bereits abgestimmt hat
        if (userId !in votedUser) {
            // Hinzufügen einer Stimme basierend auf dem Stimmentyp
            when (vote) {
                VoteType.OPTION1 -> votesOption1++     // Erhöhung der TRUE-Stimmen
                VoteType.OPTION2 -> votesOption2++ // Erhöhung der NEUTRAL-Stimmen
                VoteType.OPTION3 -> votesOption3++   // Erhöhung der FALSE-Stimmen
                VoteType.OPTION4 -> votesOption4++
            }
            totalVotes++   // Gesamtanzahl der Stimmen erhöhen
            votedUser.add(userId)  // Benutzer zur Liste der abgestimmten Benutzer hinzufügen
            return true   // Rückgabe von 'true', da die Stimme erfolgreich hinzugefügt wurde
        }
        // Rückgabe von 'false', falls der Benutzer bereits abgestimmt hat
        return false
    }

    /* Die Methode `addUserToQuestionUpDownVote` ermöglicht das Hinzufügen von Zustimmungen oder Ablehnungen zur Umfrage selbst,
       nicht zu den einzelnen Antwortoptionen. Sie folgt einem ähnlichen Prinzip wie `addVote`, verhindert Mehrfachabstimmungen
       und verarbeitet zwei Arten von Stimmen: Upvotes und Downvotes. */

    fun addUserToQuestionUpDownVote(userId: String, questionVote: VoteTypeQuestion): Boolean {
        if (userId !in votedQuestionUser) {
            when (questionVote) {
                VoteTypeQuestion.OPTIONUP -> questionUpVotes++
                VoteTypeQuestion.OPTIONDOWN -> questionDownVotes++
            }
            votedQuestionUser.add(userId)
            return true
        }
        return false
    }

    /* `totalUpDownVotes` berechnet das Nettoergebnis der Upvotes und Downvotes.
       Dies ist nützlich, um eine allgemeine Bewertung der Umfrage selbst zu erhalten,
       unabhängig von den Antworten auf die Umfrage. */

    fun totalUpDownVotes(): String {
        votedQuestionResult = questionUpVotes - questionDownVotes
        return votedQuestionResult.toString()
    }

    /* Die Methode `getFormattedTime` konvertiert den Zeitstempel der Umfrage in ein lesbares Datum und Uhrzeit-Format.
       Dies ist wichtig für die Benutzerfreundlichkeit, da Zeitstempel in ihrer rohen Form für Endbenutzer oft schwer zu interpretieren sind. */

    fun getFormattedTime(): String {
        val millis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val date = java.util.Date(millis)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    /* `toMap` konvertiert das `SurveyItem` Objekt in eine HashMap.
       Diese Methode ist besonders nützlich für die Interaktion mit Datenbanken wie Firebase,
       die Daten in Form von Schlüssel-Wert-Paaren speichern. */

    fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "surveyid" to surveyid,
            "userId" to userId,
            "timestamp" to timestamp, // Beachten Sie, dass der Timestamp in einen geeigneten Format umgewandelt werden muss, wenn er nicht bereits ein String ist.
            "publishedBy" to publishedBy,
            "isPublished" to isPublished,
            "header" to header,
            "category" to category,
            "surveyText" to surveyText,
            "answerOption1" to answerOption1,
            "answerOption2" to answerOption2,
            "answerOption3" to answerOption3,
            "answerOption4" to answerOption4,
            "surveySaved" to surveySaved,
            "totalVotes" to totalVotes,
            "votesOption1" to votesOption1,
            "votesOption2" to votesOption2,
            "votesOption3" to votesOption3,
            "votesOption4" to votesOption4,
            "hasVoted" to hasVoted,
            "votedUser" to votedUser.toList(), // Umwandlung des Sets in eine Liste, da Sets nicht direkt in Firebase gespeichert werden können.
            "showPercentage" to showPercentage,
            "votedQuestionResult" to votedQuestionResult,
            "votedQuestionUser" to votedQuestionUser.toList(),
            "questionUpVotes" to questionUpVotes,
            "questionDownVotes" to questionDownVotes,
            "hasVotedQuestion" to hasVotedQuestion
        )
    }
}


//Listen sind problematisch bei der ROOM-Datenbank ggf. Anpassungen erforderlich
