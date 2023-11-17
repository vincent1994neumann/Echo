package com.example.abschlussprojektandroide.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : Fragment() {
    private lateinit var binding : FragmentSplashScreenBinding
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSplashScreenBinding.inflate(inflater,container,false)

        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
        },1800)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.quoteOfTheDay.observe(viewLifecycleOwner){quote->
            binding.tvApiCallQuote.text = "\"${quote}\" - ${quote.author}"
        }

    }
}