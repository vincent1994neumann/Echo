package com.example.abschlussprojektandroide.data.dataclass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.Timestamp

class AppRepository {

    private val _survey = MutableLiveData<List<SurveyItem>>()
    val survey: LiveData<List<SurveyItem>>
        get() = _survey

    var firestore = RepositoryFirestore()
    private var apiRepository = ApiRepository()

    val quoteOfTheDay = apiRepository.quoteOfTheDay


    fun loadSurveys() {
        FirebaseFirestore.getInstance().collection("SurveyItem")
            .get()
            .addOnSuccessListener { result ->
                val surveyList = result.map { document ->
                    document.toObject(SurveyItem::class.java).apply {
                        surveyid = document.id
                    }
                }
                _survey.postValue(surveyList)
            }
            .addOnFailureListener { exeption ->
                Log.w("AppRepository", "Error loading surveys", exeption)
            }
        /*
        firestoreSurveyItem.collection("SurveyItem")
            .get()
            .addOnSuccessListener { result ->
                val surveyList = result.map { document ->
                    // Konvertieren Sie das DocumentSnapshot-Objekt in ein SurveyItem-Objekt.
                    // Stellen Sie sicher, dass Sie eine passende Methode in Ihrer SurveyItem-Klasse haben,
                    // um ein DocumentSnapshot in ein SurveyItem zu konvertieren.
                   document.toObject(SurveyItem::class.java).apply {
                   surveyid = document.id // Setzen Sie die surveyid auf die Document-ID.
                    }
                }
                _survey.value = surveyList // Aktualisieren Sie LiveData mit der geladenen Liste.
            }
            .addOnFailureListener { exception ->
                Log.w("AppRepository", "Error loading surveys", exception)
            }


        _survey.value = listOf(
            SurveyItem(
                surveyid = "1",
                userId = "",
                timestamp = Timestamp.now(),
                publishedBy = "Alice",
                isPublished = true,
                header = "Investitionen in erneuerbare Energien",
                category = "Umwelt",
                surveyText = "Sollten Investitionen in erneuerbare Energien erhöht werden?",
                totalVotes = 5,
                votesOption1 = 2,
                votesOption2 = 1,
                votesOption3 = 1,
                votesOption4 = 1,
                questionUpVotes = 80,
                questionDownVotes = 20,
                answerOption1 = "Erhöhen",
                answerOption2 = "Beibehalten",
                answerOption3 = "Senken",
                answerOption4 = "Sonstiges"
            ),
            SurveyItem(
                surveyid = "2",
                userId = "",
                timestamp = Timestamp.now(),
                publishedBy = "Cassandra",
                isPublished = true,
                header = "Alternative zum Auto: Öffentlicher Nahverkehr",
                category = "Verkehr",
                surveyText = "Ist der öffentliche Nahverkehr eine gute Alternative zum Auto?",
                totalVotes = 75,
                votesOption1 = 65,
                votesOption2 = 5,
                votesOption3 = 5,
                votesOption4 = 0,
                questionUpVotes = 60,
                questionDownVotes = 15,
                answerOption1 = "Gute Alternative",
                answerOption2 = "Teilweise",
                answerOption3 = "Keine Alternative",
                answerOption4 = "Sonstiges"
            ),
            SurveyItem(
                surveyid = "3",
                userId = "",
                timestamp = Timestamp.now(),
                publishedBy = "David",
                isPublished = true,
                header = "Verschärfung der Gesetze für Online-Streaming",
                category = "Technologie",
                surveyText = "Sollten die Gesetze für Online-Streaming verschärft werden?",
                totalVotes = 85,
                votesOption1 = 40,
                votesOption2 = 20,
                votesOption3 = 25,
                votesOption4 = 0,
                questionUpVotes = 70,
                answerOption1 = "Verschärfen",
                answerOption2 = "Unverändert lassen",
                answerOption3 = "Lockern",
                answerOption4 = "Sonstiges"
            ),
            SurveyItem(
                surveyid = "4",
                userId = "",
                timestamp = Timestamp.now(),
                publishedBy = "Eva",
                isPublished = true,
                header = "Die 4-Tage-Arbeitswoche",
                category = "Arbeit",
                surveyText = "Würde eine 4-Tage-Arbeitswoche die Produktivität steigern?",
                totalVotes = 120,
                votesOption1 = 80,
                votesOption2 = 25,
                votesOption3 = 15,
                votesOption4 = 0,
                questionUpVotes = 100,
                questionDownVotes = 20,
                answerOption1 = "Ja",
                answerOption2 = "Neutral",
                answerOption3 = "Nein",
                answerOption4 = "Sonstiges"
            ),
            SurveyItem(
                surveyid = "5",
                userId = "",
                timestamp = Timestamp.now(),
                publishedBy = "Felix",
                isPublished = true,
                header = "Staatliche Finanzierung von Bildung",
                category = "Bildung",
                surveyText = "Sollte Bildung vollständig staatlich finanziert werden?",
                totalVotes = 90,
                votesOption1 = 50,
                votesOption2 = 20,
                votesOption3 = 20,
                votesOption4 = 0,
                questionUpVotes = 75,
                questionDownVotes = 15,
                answerOption1 = "Ja",
                answerOption2 = "Neutral",
                answerOption3 = "Nein",
                answerOption4 = "Sonstiges"
            ),

            SurveyItem(
                surveyid = "6",
                userId = "",
                timestamp = Timestamp.now(),
                publishedBy = "Grace",
                isPublished = false,
                header = "Globale Klimaabgabe",
                category = "Umwelt",
                surveyText = "Sollte es eine globale Klimaabgabe geben?",
                totalVotes = 60,
                votesOption1 = 30,
                votesOption2 = 15,
                votesOption3 = 15,
                votesOption4 = 0,
                questionUpVotes = 55,
                questionDownVotes = 5,
                answerOption1 = "True",
                answerOption2 = "Neutral",
                answerOption3 = "False",
                answerOption4 = "Sonstiges"
            ),
            SurveyItem(
                surveyid = "10",
                userId = "",
                timestamp = Timestamp.now(),
                publishedBy = "Ben",
                isPublished = false,
                header = "Datenschutzgesetze",
                category = "Politik",
                surveyText = "Brauchen wir strengere Datenschutzgesetze?",
                totalVotes = 50,
                votesOption1 = 45,
                votesOption2 = 5,
                votesOption3 = 0,
                votesOption4 = 0,
                questionUpVotes = 50,
                questionDownVotes = 0,
                answerOption1 = "True",
                answerOption2 = "Neutral",
                answerOption3 = "False",
                answerOption4 = "Sonstiges"
            )
        )

         */
    }

   suspend fun loadQuoteOfTheDay(){
        apiRepository.loadQuoteOfTheDay()
    }


}

