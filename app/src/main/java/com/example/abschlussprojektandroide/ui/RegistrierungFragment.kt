package com.example.abschlussprojektandroide.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentRegistrierungBinding

class RegistrierungFragment : Fragment() {
    private lateinit var binding: FragmentRegistrierungBinding // Späte Initialisierung der Binding-Variable für dieses Fragment
    private val viewModel: SharedViewModel by activityViewModels() // Verwendung eines gemeinsamen ViewModel für die Aktivität

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrierungBinding.inflate(inflater, container, false) // Inflate des Layouts für dieses Fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rotate()
        // Beobachtet den aktuellen App-Benutzer und navigiert zum Home-Fragment, falls bereits eingeloggt
        viewModel.currentAppUser.observe(viewLifecycleOwner) {
            if (it != null && viewModel.currentUser.value != null) {
                findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToHomeFragment())
            }
        }

        // Beobachtet Änderungen am aktuellen Firebase-Benutzer
        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.fetchCurrentUser()
            }
        }

        // Event-Handler für den Zurück-zum-Login-Button
        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToLoginFragment())
        }

        // Event-Handler für den Registrierungs-Button
        binding.btnSignUp.setOnClickListener {
            val email = binding.textInputemail.text.toString()
            val password = binding.textInputpassword.text.toString()
            val confirmPassword = binding.textInputpasswordrepeat.text.toString()
            val username = binding.textInputusernameReg.text.toString()
            register(email, password, confirmPassword, username)
        }
    }

    private fun register(email: String, password: String, confirmPassword: String, username: String) {
        // Überprüft, ob die Eingabefelder ausgefüllt sind und ob die Passwörter übereinstimmen
        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                viewModel.register(email, password, confirmPassword, username, requireContext())
            } else {
                Toast.makeText(requireContext(), "Password Is Not Matching!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "Please enter your login details.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rotate (){
        val animation = ObjectAnimator.ofFloat(binding.ivLogoecho, View.ROTATION,0f,360f)
        animation.duration = 2000
        animation.repeatCount = 20
        animation.repeatMode = ObjectAnimator.RESTART
        animation.start()
    }
}
