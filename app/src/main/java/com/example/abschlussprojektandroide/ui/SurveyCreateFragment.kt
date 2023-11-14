package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.databinding.FragmentSurveyCreateBinding


class SurveyCreateFragment : Fragment() {
    private lateinit var binding: FragmentSurveyCreateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSurveyCreateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}

//Implementieren Sie Logik in Ihrem Fragment, um die ausgewählte Anzahl von Antwortmöglichkeiten zu verarbeiten.