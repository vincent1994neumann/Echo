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
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.adapter.SurveyAdapter
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var surveyAdapter: SurveyAdapter
    private var searchJob: Job? = null //Delay bei der Suche

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var updateSurveyItem = { surveyItem: SurveyItem ->
            viewModel.updateSurveyItem(surveyItem)
        }

        // Definieren Sie den onSaveClicked-Callback
        val onSaveClicked: (String, Boolean) -> Unit = { surveyId, shouldSave ->
            viewModel.updateFavoriteSurveys(surveyId, shouldSave)
        }

        surveyAdapter = SurveyAdapter(
            listOf(),
            viewModel.currentUser.value!!.uid,
            updateSurveyItem,
            onSaveClicked,
            viewModel.currentAppUser.value?.savedSurveys?: mutableListOf()
        )

        binding.rvSearch.adapter = surveyAdapter


        binding.tiSearchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
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
        searchJob?.cancel() // Bisherige Suche abbrechen
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(500) // Warten für 500 Millisekunden
            viewModel.getFilteredSurveysForSearch(query).observe(viewLifecycleOwner) { surveyItems ->
                surveyAdapter.updateData(surveyItems)
            }
            // Weitere Suchmethoden, falls nötig
        }
    }
}