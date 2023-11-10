package com.example.abschlussprojektandroide.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.databinding.FragmentRegistrierungBinding
import com.google.firebase.auth.FirebaseAuth


class RegistrierungFragment : Fragment() {
    private lateinit var binding: FragmentRegistrierungBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrierungBinding.inflate(inflater,container,false)
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       if (firebaseAuth.currentUser != null){
            findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToHomeFragment())
              }

        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToLoginFragment())
        }
        binding.btnRegistieren.setOnClickListener {
            val email= binding.textInputemail.text.toString()
            val password= binding.textInputpassword.text.toString()
            val confirmPassword= binding.textInputpasswordrepeat.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        /*isadded
                        In dem Kontext, in dem ich es vorgeschlagen habe, wird isAdded verwendet, um sicherzustellen,
                        dass das Fragment noch aktiv ist, wenn der asynchrone Callback addOnCompleteListener der Firebase-Aufruf abgeschlossen wird.
                        Wenn das Fragment nicht mehr hinzugefügt ist (z.B. wenn der Benutzer das Fragment verlassen hat, bevor der Callback ausgeführt wurde),
                        dann sollten Sie keine Operationen durchführen, die von einem Kontext abhängen, da dies zu einer IllegalStateException führen kann.
                         */
                        if (task.isSuccessful && isAdded) {
                            var navController = findNavController()
                            // Navigation zum HomeFragment, wenn die Registrierung erfolgreich war
                            navController.navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToHomeFragment())
                        }

                        else {
                            val error = task.exception?.message ?: "Unbekannter Fehler"
                            Toast.makeText(requireContext(), "Fehler beim Registrieren: $error", Toast.LENGTH_SHORT).show()
                        }
                    }

                }else {
                    Toast.makeText(requireContext(), "Password Is Not Matching!", Toast.LENGTH_SHORT).show()
                }
            } else {
            Toast.makeText(requireContext(), "Empty Fields Are Not Allowed!", Toast.LENGTH_SHORT).show()
        }
            }


        }


    }
