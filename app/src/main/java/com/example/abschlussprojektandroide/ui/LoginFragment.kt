package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegistieren.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrierungFragment())
        }

        binding.btnLogin.setOnClickListener {


        }
    }
}

