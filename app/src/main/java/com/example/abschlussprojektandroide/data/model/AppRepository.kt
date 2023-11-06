package com.example.abschlussprojektandroide.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektandroide.data.dataclass.SurveyItem

class AppRepository {

    private val _survey = MutableLiveData<List<SurveyItem>>()
    val survey : LiveData<List<SurveyItem>>
        get() = _survey

    init {
   //     loadSurveys()
    }


    fun loadSurveys() {
        _survey.value = listOf(
            SurveyItem(
                surveyid = 1,
                timestamp = "2023-11-06T10:00:00Z",
                publishedBy ="Alice",
                isPublished = true,
                header = "Sollten wir mehr in erneuerbare Energien investieren?",
                category = "Umwelt",
                imageUrl = "http://example.com/image1.jpg",
                surveyText = "Ist es an der Zeit, die Investitionen in erneuerbare Energien zu erhöhen?",
                totalVotes = 100,
                votesTrue = 70,
                votesNeutral = 20,
                votesFalse = 10,
                questionUpvotes = 80,
                questionDownvotes = 20,
            ),
            SurveyItem(
                surveyid = 10,
                timestamp = "2023-11-06T19:00:00Z",
                publishedBy = "Ben",
                isPublished = false,
                header = "Brauchen wir strengere Datenschutzgesetze?",
                category = "Politik",
                imageUrl = null,
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

