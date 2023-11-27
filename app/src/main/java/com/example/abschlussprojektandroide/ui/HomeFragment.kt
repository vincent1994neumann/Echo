package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abschlussprojektandroide.adapter.SurveyAdapter
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {
    // Deklaration der Variablen für das Binding und das ViewModel
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: SharedViewModel by activityViewModels()
    // Deklaration des SurveyAdapters als spät initialisierte Variable.
    // Dieser Adapter wird verwendet, um die Daten (Umfragen) in der RecyclerView anzuzeigen.
    // Die Variable wird später initialisiert, da sie Daten benötigt, die erst nach der Erstellung des Fragments verfügbar sind.
    private lateinit var surveyAdapter: SurveyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Initialisieren des View Bindings
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lambda-Funktion, die eine SurveyItem-Instanz an das ViewModel zur Aktualisierung weiterleitet
        var updateSurveyItem = { surveyItem: SurveyItem ->
            viewModel.updateSurveyItem(surveyItem)
        }

        // Definieren Sie den onSaveClicked-Callback
        val onSaveClicked: (String, Boolean) -> Unit = { surveyId, shouldSave ->
            viewModel.updateFavoriteSurveys(surveyId, shouldSave)
        }

        // Beobachten der aktuellen App-Benutzerdaten und Initialisieren des Adapters, wenn Benutzerdaten vorhanden sind
        viewModel.currentAppUser.observe(viewLifecycleOwner) {
            if (viewModel.currentUser.value != null) {
                surveyAdapter = SurveyAdapter(
                    listOf(),
                    viewModel.currentUser.value!!.uid,
                    updateSurveyItem,
                    onSaveClicked,
                    viewModel.currentAppUser.value?.savedSurveys?: mutableListOf()
                )
            }
            // RecyclerView-Setup mit dem SurveyAdapter
            binding.rvHome.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = surveyAdapter
            }
        }

        // Beobachten von Änderungen in der Umfrageliste und Aktualisieren des Adapters bei Änderungen
        viewModel.survey.observe(viewLifecycleOwner) { surveyList ->
            Log.d("HomeFragment", viewModel.currentAppUser.value.toString())
            surveyAdapter.updateData(surveyList)
        }

        // Setzen des Click Listeners für den Floating Action Button, um zur Umfrageerstellungsseite zu navigieren
        binding.btnFloatingNewVote.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSurveyCreateFragment())
        }


        viewModel.currentAppUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                setUpTabLayout(user.userId)
            }
        }
    }



    private fun setUpTabLayout(surveyId:String){
        binding.tlTabbarHome.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0-> viewModel.getLatestSurveysForHomepage()
                        .observe(viewLifecycleOwner){surveys ->
                            updateAdapter(surveys)
                        }
                    1-> viewModel.getSurveysUserNotVoted()
                        .observe(viewLifecycleOwner){surveys ->
                            updateAdapter(surveys)
                        }
                    2-> viewModel.getSurveysByQuestionCount()
                        .observe(viewLifecycleOwner){surveys ->
                            updateAdapter(surveys)
                        }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun updateAdapter(surveys: List<SurveyItem>) {
        // Nehmen Sie an, dass viewModel.currentUser und viewModel.currentAppUser bereits beobachtet werden und ihre Werte haben.
        val currentUserId = viewModel.currentUser.value?.uid ?: ""
        val savedSurveys = viewModel.currentAppUser.value?.savedSurveys ?: mutableListOf()

        // Initialisierung des Adapters mit den neuen Daten
        binding.rvHome.adapter = SurveyAdapter(
            surveys,
            currentUserId,
            { surveyItem -> viewModel.updateSurveyItem(surveyItem) }, // updateSurveyItem Callback
            { surveyId, shouldSave -> viewModel.updateFavoriteSurveys(surveyId, shouldSave) }, // onSaveClicked Callback
            savedSurveys
        )
    }
}
