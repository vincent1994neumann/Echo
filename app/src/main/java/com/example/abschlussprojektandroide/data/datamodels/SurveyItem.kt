package com.example.abschlussprojektandroide.data.datamodels

data class SurveyItem(
    val surveyid: Int, // Eindeutige ID, z.B. von einer Datenbank
    val timestamp: String, // Zeitstempel des Surveys
    val publishedBy: User, // Name des Veröffentlichers
    val isPublished: Boolean = false, // Status der Umfrage: Veröffentlicht oder nicht veröffentlicht.
    val header: String, // Überschrift des Surveys
    val category: String, // Kategorie des Surveys (z.B. "Umwelt")
    val imageUrl: String?, // URL oder Pfad zum Bild, wenn vorhanden
    val surveyText: String, // Text der eigentlichen Umfrage
    val totalVotes: Int = 0, // Gesamtanzahl der Stimmen
    val votesTrue: Int = 0, // Anzahl der "true" Stimmen
    val votesNeutral: Int = 0, // Anzahl der "neutral" Stimmen
    val votesFalse: Int = 0, // Anzahl der "false" Stimmen
    val questionUpvotes: Int = 0, // Anzahl der Zustimmungen für die Umfrage (für das Ranking)
    val questionDownvotes: Int = 0, // Anzahl der Ablehnungen für die Umfrage (für das Ranking)

    //val usersVoted: MutableSet<String> = mutableSetOf() // Set von User-IDs, die bereits abgestimmt haben
    val userVoted: MutableMap<String, String> = mutableMapOf() // Map von User-IDs zu ihrer gewählten Antwort ("true", "neutral" oder "false")
) {
    val totalResponseVotes get() = votesTrue + votesNeutral + votesFalse

    // Methode, um zu überprüfen, ob ein Benutzer bereits abgestimmt hat
    fun hasUserVoted(userId: String) = userVoted.containsKey(userId)

    // Methode, um die gewählte Antwort eines Benutzers zu erhalten
    fun getUserVote(userId: String) = userVoted[userId]
    fun registerVote(userId: String, vote: String): Boolean {
        if (!hasUserVoted(userId)) {
            userVoted[userId] = vote
            return true
        }
        return false
    }
}
