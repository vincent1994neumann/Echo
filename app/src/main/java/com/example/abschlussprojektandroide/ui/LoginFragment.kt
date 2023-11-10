package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (firebaseAuth.currentUser != null){
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }


        binding.btnRegistieren.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrierungFragment())
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.textInputemail.text.toString()
            val password = binding.textInputpassword.text.toString()
            login(email, password)

        }
    }

    private fun login(email: String, password:String){
        if(email.isNotEmpty() && password.isNotEmpty()){
            val email= binding.textInputemail.text.toString()
            val password= binding.textInputpassword.text.toString()

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful && isAdded) {
                    // Navigieren zum nächsten Fragment, wenn der Login erfolgreich war
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                } else {
                    // Zeigen Sie eine Fehlermeldung an, wenn der Login fehlschlägt
                    Toast.makeText(context, "Login fehlgeschlagen: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "E-Mail und Passwort dürfen nicht leer sein", Toast.LENGTH_LONG).show()
        }
    }
    }


