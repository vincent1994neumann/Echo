package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.adapter.SurveyAdapter
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentProfilBinding
import com.google.android.material.tabs.TabLayout


class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentAppUser.observe(viewLifecycleOwner) {
            binding.headerProfil.text = it?.username
            binding.tvCountSurveysCreated.text = it?.userCreatedSurveys?.size.toString()
            binding.tvCountSavedSurveys.text = it?.savedSurveys?.size.toString()
            Log.d("Anzahl Survey"," ${binding.tvCountSurveysCreated.text}")
        }

        val rVc = binding.rvProfil
        rVc.layoutManager = LinearLayoutManager(context)
        rVc.setHasFixedSize(true)


        // Setze hier die initialen Daten für "My Posts"
        val currentUserId = viewModel.currentUser.value?.uid ?: ""
        viewModel.getUserCreatedSurveys(currentUserId)
            .observe(viewLifecycleOwner) { surveys ->
                updateAdapter(surveys)
            }

        binding.btnFloatingNewVoteProfil.setOnClickListener {
            findNavController().popBackStack()
            viewModel.logout()
            Toast.makeText(context, "Erfolgreich ausgeloggt", Toast.LENGTH_SHORT).show()

        }

        viewModel.currentAppUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                setUpTabLayout(user.userId)
            }
        }

    }

    private fun setUpTabLayout(userId: String) {
        binding.tlTabbarProfil.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> viewModel.getUserCreatedSurveys(userId)
                        .observe(viewLifecycleOwner) { surveys ->
                            updateAdapter(surveys)
                        }

                    1 -> viewModel.getUserSavedSurveys(userId)
                        .observe(viewLifecycleOwner) { surveys ->
                            updateAdapter(surveys)
                        }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Optional, wenn nötig
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Optional, wenn nötig
            }
        })
    }

    private fun updateAdapter(surveys: List<SurveyItem>) {
        // Nehmen Sie an, dass viewModel.currentUser und viewModel.currentAppUser bereits beobachtet werden und ihre Werte haben.
        val currentUserId = viewModel.currentUser.value?.uid ?: ""
        val savedSurveys = viewModel.currentAppUser.value?.savedSurveys ?: mutableListOf()

        // Initialisierung des Adapters mit den neuen Daten
        binding.rvProfil.adapter = SurveyAdapter(
            surveys,
            currentUserId,
            { surveyItem -> viewModel.updateSurveyItem(surveyItem) }, // updateSurveyItem Callback
            { surveyId, shouldSave -> viewModel.updateFavoriteSurveys(surveyId, shouldSave) }, // onSaveClicked Callback
            savedSurveys
        )
    }
}