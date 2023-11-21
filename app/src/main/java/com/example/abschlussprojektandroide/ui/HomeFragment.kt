package com.example.abschlussprojektandroide.ui

import android.os.Bundle
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

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel:SharedViewModel by activityViewModels()
    private lateinit var  surveyAdapter: SurveyAdapter


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var updateSurveyItem = { surveyItem: SurveyItem ->
            viewModel.updateSurveyItem(surveyItem)
        }
        if (viewModel.currentUser.value != null) {
            surveyAdapter = SurveyAdapter(listOf(), viewModel.currentUser.value!!.uid,updateSurveyItem)
        } // Initial leer
        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = surveyAdapter
        }

        viewModel.survey.observe(viewLifecycleOwner) { surveyList ->
            surveyAdapter.updateData(surveyList) // Aktualisieren Sie Ihren Adapter mit der neuen Liste
        }

        binding.btnFloatingNewVote.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSurveyCreateFragment()) }
    }
}