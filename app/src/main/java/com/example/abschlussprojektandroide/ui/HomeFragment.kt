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
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel:SharedViewModel by activityViewModels()

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


        val rVc = binding.rvHome
        rVc.layoutManager = LinearLayoutManager(context)
        rVc.setHasFixedSize(true)

        viewModel.survey.observe(viewLifecycleOwner){rVc.adapter = SurveyAdapter(it)}



        binding.btnFloatingNewVote.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSurveyCreateFragment()) }
    }
}