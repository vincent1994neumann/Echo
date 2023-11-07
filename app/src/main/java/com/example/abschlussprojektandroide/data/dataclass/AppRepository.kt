package com.example.abschlussprojektandroide.data.dataclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektandroide.R
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
                publishedBy ="Alice",
                isPublished = true,
                header = "Sollten wir mehr in erneuerbare Energien investieren?",
                category = "Umwelt",
                surveyText = "Ist es an der Zeit, die Investitionen in erneuerbare Energien zu erhöhen?",
                totalVotes = 100,
                votesTrue = 70,
                votesNeutral = 20,
                votesFalse = 10,
                questionUpvotes = 80,
                questionDownvotes = 20,
            ),
            SurveyItem(
                surveyid = 2,
                timestamp = "2023-11-06 21:55",
                publishedBy = "Cassandra",
                isPublished = true,
                header = "Ist öffentlicher Nahverkehr eine gute Alternative zum Auto?",
                category = "Verkehr",
                surveyText = "Sollten Städte mehr in den öffentlichen Nahverkehr investieren?",
                totalVotes = 75,
                votesTrue = 65,
                votesNeutral = 5,
                votesFalse = 5,
                questionUpvotes = 60,
                questionDownvotes = 15
            ),
            SurveyItem(
                surveyid = 3,
                timestamp = "2023-11-06 21:05",
                publishedBy = "David",
                isPublished = true,
                header = "Müssen die Gesetze für Online-Streaming verschärft werden?",
                category = "Technologie",
                surveyText = "Wie sollten Urheberrechtsverletzungen im Internet gehandhabt werden?",
                totalVotes = 85,
                votesTrue = 40,
                votesNeutral = 20,
                votesFalse = 25,
                questionUpvotes = 70,
                questionDownvotes = 15
            ),
            SurveyItem(
                surveyid = 4,
                timestamp = "2023-11-06 23:48",
                publishedBy = "Eva",
                isPublished = true,
                header = "Ist die 4-Tage-Arbeitswoche effektiver?",
                category = "Arbeit",
                surveyText = "Würde eine 4-Tage-Arbeitswoche die Produktivität verbessern?",
                totalVotes = 120,
                votesTrue = 80,
                votesNeutral = 25,
                votesFalse = 15,
                questionUpvotes = 100,
                questionDownvotes = 20
            ),
            SurveyItem(
                surveyid = 5,
                timestamp = "2023-11-06 06:36",
                publishedBy = "Felix",
                isPublished = true,
                header = "Sollte Bildung komplett staatlich finanziert werden?",
                category = "Bildung",
                surveyText = "Ist freier Zugang zu Bildung für jeden notwendig?",
                totalVotes = 90,
                votesTrue = 50,
                votesNeutral = 20,
                votesFalse = 20,
                questionUpvotes = 75,
                questionDownvotes = 15
            ),
            SurveyItem(
                surveyid = 6,
                timestamp = "2023-11-06 13:55",
                publishedBy = "Grace",
                isPublished = false,
                header = "Sollte es eine globale Klimaabgabe geben?",
                category = "Umwelt",
                surveyText = "Wäre eine weltweite Abgabe eine Lösung für den Klimawandel?",
                totalVotes = 60,
                votesTrue = 30,
                votesNeutral = 15,
                votesFalse = 15,
                questionUpvotes = 55,
                questionDownvotes = 5
            ),
            SurveyItem(
                surveyid = 10,
                timestamp = "2023-11-06 14:00",
                publishedBy = "Ben",
                isPublished = false,
                header = "Brauchen wir strengere Datenschutzgesetze?",
                category = "Politik",
                surveyText = "Wie wichtig ist Ihnen der Datenschutz?",
                totalVotes = 50,
                votesTrue = 45,
                votesNeutral = 5,
                votesFalse = 0,
                questionUpvotes = 50,
                questionDownvotes = 0,
            )
        )
    }

}

