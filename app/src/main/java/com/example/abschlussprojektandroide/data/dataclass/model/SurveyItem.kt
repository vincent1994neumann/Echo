package com.example.abschlussprojektandroide.data.dataclass.model

data class SurveyItem(
    val surveyid: Int, // Eindeutige ID, z.B. von einer Datenbank
    val userId: Int = 0,
    val timestamp: String, // Zeitstempel des Surveys
    val publishedBy: String, // Name des Veröffentlichers via ID
    val isPublished: Boolean = false, // Status der Umfrage: Veröffentlicht oder nicht veröffentlicht.
    val header: String, // Überschrift des Surveys
    val category: String, // Kategorie des Surveys (z.B. "Umwelt")
    val surveyText: String, // Text der eigentlichen Umfrage

    var totalVotes: Int = 0, // Gesamtanzahl der Stimmen
    var votesTrue: Int = 0, // Anzahl der "true" Stimmen
    var votesNeutral: Int = 0, // Anzahl der "neutral" Stimmen
    var votesFalse: Int = 0, // Anzahl der "false" Stimmen
    var hasVoted:Boolean = false,
    val votedUser: MutableSet<Int> = mutableSetOf(), // Abfragen ob USer bereits abgestimmt hat, Set von User-IDs, die bereits abgestimmt haben

    var questionUpVotes: Int = 0, // Anzahl der Zustimmungen für die Umfrage (für das Ranking)
    var questionDownVotes: Int = 0, // Anzahl der Ablehnungen für die Umfrage (für das Ranking)
    var hasVotedQuestion: Boolean =false,


    ){
    fun percentageTrue ():String{
        var trueVotePercentage = ((votesTrue.toDouble()/totalVotes.toDouble())*100)
        return String.format("%.2f", trueVotePercentage) + "%"
    }

    fun percentageNeutral ():String{
        var neutralVotePercentage = ((votesNeutral.toDouble()/totalVotes.toDouble())*100)
        return String.format("%.2f", neutralVotePercentage) + "%"
    }

    fun percentageFalse ():String{
        var falseVotePercentage = ((votesFalse.toDouble()/totalVotes.toDouble())*100)
        return String.format("%.2f", falseVotePercentage) + "%"
    }

    fun totalUpDownVotes ():String{
        var totalVotesQuestion= questionUpVotes-questionDownVotes
        return totalVotesQuestion.toString()
    }
}



//Listen sind problematisch bei der ROOM-Datenbank ggf. Anpassungen erforderlich
