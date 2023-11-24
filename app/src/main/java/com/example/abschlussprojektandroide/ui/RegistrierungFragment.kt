package com.example.abschlussprojektandroide.ui

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
    private lateinit var binding: FragmentRegistrierungBinding
    private val viewModel:SharedViewModel by activityViewModels()

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



        viewModel.currentUser.observe(viewLifecycleOwner){
            if (it!= null) {
            findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToHomeFragment())
              }
        }

        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(RegistrierungFragmentDirections.actionRegistrierungFragmentToLoginFragment())
        }
        binding.btnSignUp.setOnClickListener {
            val email= binding.textInputemail.text.toString()
            val password= binding.textInputpassword.text.toString()
            val confirmPassword= binding.textInputpasswordrepeat.text.toString()
            val username = binding.textInputusernameReg.text.toString()
            register(email,password,confirmPassword,username)
            }

    }

    private fun register(email:String, password:String, confirmPassword:String, username:String){
        if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                viewModel.register(email, password, confirmPassword,username,requireContext())
            } else {
                Toast.makeText(
                    requireContext(),
                    "Password Is Not Matching!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    }
