package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.adapter.SurveyAdapter
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth


class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private val viewModel:SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rVc = binding.rvProfil
        rVc.layoutManager = LinearLayoutManager(context)
        rVc.setHasFixedSize(true)

        viewModel.survey.observe(viewLifecycleOwner){rVc.adapter = SurveyAdapter(it)}

        binding.btnFloatingNewVoteProfil.setOnClickListener{
            viewModel.logout()
            findNavController().navigate(ProfilFragmentDirections.actionProfilFragmentToLoginFragment())
            Toast.makeText(context, "Erfolgreich ausgeloggt", Toast.LENGTH_SHORT).show()

        }

    }




}