package com.example.abschlussprojektandroide.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.databinding.FragmentLoginBinding
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import androidx.fragment.app.activityViewModels

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding // Späte Initialisierung der Binding-Variable für diese Ansicht
    private val viewModel: SharedViewModel by activityViewModels() // Verwendung eines gemeinsamen ViewModel für die Aktivität

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false) // Inflate des Layouts für dieses Fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beobachten von Änderungen am aktuellen App-Benutzer und Weiterleitung, falls bereits angemeldet
        viewModel.currentAppUser.observe(viewLifecycleOwner) {
            if (it != null && viewModel.currentUser.value != null) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }
        }

        // Beobachten von Änderungen am aktuellen Firebase-Benutzer
        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.fetchCurrentUser()
            }
        }

        // Event-Handler für den Registrierungsbutton
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrierungFragment())
        }

        // Event-Handler für den Login-Button
        binding.btnLogin.setOnClickListener {
            val email = binding.textInputemail.text.toString()
            val password = binding.textInputpassword.text.toString()
            login(email, password, requireContext())
        }
    }

    private fun login(email: String, password: String, context: Context) {
        // Überprüfung der Eingabefelder und Ausführung des Login-Vorgangs
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(email, password, context)
            Log.d("RepositoryFirestore", "Login erfolgreich für Benutzer: ${email}")
        } else {
            Log.d("RepositoryFirestore", "Login nicht erfolgreich für Benutzer: ${email}")
            Toast.makeText(context, "Email and password cannot be empty.", Toast.LENGTH_LONG).show()
        }
    }



}

