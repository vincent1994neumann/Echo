package com.example.abschlussprojektandroide.data.dataclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem

class AppRepository {

    private val _survey = MutableLiveData<List<SurveyItem>>()
    val survey : LiveData<List<SurveyItem>>
        get() = _survey



    fun loadSurveys() {
        _survey.value = listOf(
            SurveyItem(
                surveyid = 1,
                timestamp = "2023-11-06 13:47",
                publishedBy = "Alice",
                isPublished = true,
                header = "Investitionen in erneuerbare Energien",
                category = "Umwelt",
                surveyText = "Sollten Investitionen in erneuerbare Energien erhöht werden?",
                totalVotes = 0,
                votesTrue = 0,
                votesNeutral = 0,
                votesFalse = 0,
                questionUpVotes = 80,
                questionDownVotes = 20,
            ),
            SurveyItem(
                surveyid = 2,
                timestamp = "2023-11-06 21:55",
                publishedBy = "Cassandra",
                isPublished = true,
                header = "Alternative zum Auto: Öffentlicher Nahverkehr",
                category = "Verkehr",
                surveyText = "Ist der öffentliche Nahverkehr eine gute Alternative zum Auto?",
                totalVotes = 75,
                votesTrue = 65,
                votesNeutral = 5,
                votesFalse = 5,
                questionUpVotes = 60,
                questionDownVotes = 15,
            ),
            SurveyItem(
                surveyid = 3,
                timestamp = "2023-11-06 21:05",
                publishedBy = "David",
                isPublished = true,
                header = "Verschärfung der Gesetze für Online-Streaming",
                category = "Technologie",
                surveyText = "Sollten die Gesetze für Online-Streaming verschärft werden?",
                totalVotes = 85,
                votesTrue = 40,
                votesNeutral = 20,
                votesFalse = 25,
                questionUpVotes = 70,
                questionDownVotes = 15,
            ),
            SurveyItem(
                surveyid = 4,
                timestamp = "2023-11-06 23:48",
                publishedBy = "Eva",
                isPublished = true,
                header = "Die 4-Tage-Arbeitswoche",
                category = "Arbeit",
                surveyText = "Würde eine 4-Tage-Arbeitswoche die Produktivität steigern?",
                totalVotes = 120,
                votesTrue = 80,
                votesNeutral = 25,
                votesFalse = 15,
                questionUpVotes = 100,
                questionDownVotes = 20,
            ),
            SurveyItem(
                surveyid = 5,
                timestamp = "2023-11-06 06:36",
                publishedBy = "Felix",
                isPublished = true,
                header = "Staatliche Finanzierung von Bildung",
                category = "Bildung",
                surveyText = "Sollte Bildung vollständig staatlich finanziert werden?",
                totalVotes = 90,
                votesTrue = 50,
                votesNeutral = 20,
                votesFalse = 20,
                questionUpVotes = 75,
                questionDownVotes = 15,
            ),
            SurveyItem(
                surveyid = 6,
                timestamp = "2023-11-06 13:55",
                publishedBy = "Grace",
                isPublished = false,
                header = "Globale Klimaabgabe",
                category = "Umwelt",
                surveyText = "Sollte es eine globale Klimaabgabe geben?",
                totalVotes = 60,
                votesTrue = 30,
                votesNeutral = 15,
                votesFalse = 15,
                questionUpVotes = 55,
                questionDownVotes = 5,
            ),
            SurveyItem(
                surveyid = 10,
                timestamp = "2023-11-06 14:00",
                publishedBy = "Ben",
                isPublished = false,
                header = "Datenschutzgesetze",
                category = "Politik",
                surveyText = "Brauchen wir strengere Datenschutzgesetze?",
                totalVotes = 50,
                votesTrue = 45,
                votesNeutral = 5,
                votesFalse = 0,
                questionUpVotes = 50,
                questionDownVotes = 0,
            )
        )
    }


}

