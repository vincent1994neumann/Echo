package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.abschlussprojektandroide.adapter.SurveyAdapter
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding // Späte Initialisierung der Binding-Variable
    private val viewModel: SharedViewModel by activityViewModels() // Gemeinsames ViewModel für die Aktivität
    private lateinit var surveyAdapter: SurveyAdapter // Späte Initialisierung des Adapters für die Umfragen
    private var searchJob: Job? = null // Variabel für verzögerte Suche

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false) // Inflate des Layouts für das Fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Callback-Methode zum Aktualisieren eines Survey-Items
        var updateSurveyItem = { surveyItem: SurveyItem ->
            viewModel.updateSurveyItem(surveyItem)
        }

        // Callback-Methode, die aufgerufen wird, wenn auf Speichern geklickt wird
        val onSaveClicked: (String, Boolean) -> Unit = { surveyId, shouldSave ->
            viewModel.updateFavoriteSurveys(surveyId, shouldSave)
        }

        // Initialisierung des SurveyAdapters mit leeren Daten
        surveyAdapter = SurveyAdapter(
            listOf(),
            viewModel.currentUser.value!!.uid,
            updateSurveyItem,
            onSaveClicked,
            viewModel.currentAppUser.value?.savedSurveys ?: mutableListOf()
        )

        binding.rvSearch.adapter = surveyAdapter

        // TextWatcher für das Suchfeld
        binding.tiSearchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Verarbeitet den eingegebenen Text nach dessen Änderung
                s?.let { searchText ->
                    performSearch(searchText.toString())
                    Log.e("Search!!!!", "${performSearch(searchText.toString())}")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nicht benötigt
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nicht benötigt
            }
        })
    }

    private fun performSearch(query: String) {
        // Führt eine Suche durch und aktualisiert die Ergebnisse
        searchJob?.cancel() // Bisherige Suche abbrechen
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(500) // Verzögerung von 500 Millisekunden
            viewModel.getFilteredSurveysForSearch(query).observe(viewLifecycleOwner) { surveyItems ->
                surveyAdapter.updateData(surveyItems)
            }
            // Weitere Suchmethoden, falls nötig
        }
    }
}
