package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.databinding.FragmentRegistrierungBinding


class RegistrierungFragment : Fragment() {
    private lateinit var binding: FragmentRegistrierungBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrierungBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToLoginFragment())
        }
        binding.btnRegistieren.setOnClickListener {


            findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToLoginFragment())
        }
    }
}