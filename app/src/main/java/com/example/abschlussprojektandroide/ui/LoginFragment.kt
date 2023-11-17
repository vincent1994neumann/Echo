package com.example.abschlussprojektandroide.ui

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }

        binding.btnRegistieren.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrierungFragment())
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.textInputemail.text.toString()
            val password = binding.textInputpassword.text.toString()
            login(email, password,requireContext())
        }
    }

    private fun login(email: String, password:String,context: Context){
        if(email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(email, password,context)
            Log.d("RepositoryFirestore", "Login erfolgreich für Benutzer: ${email}")
        } else {
            Log.d("RepositoryFirestore", "Login nicht erfolgreich für Benutzer: ${email}")
            Toast.makeText(context, "E-Mail und Passwort dürfen nicht leer sein", Toast.LENGTH_LONG).show()
        }
    }
    }


