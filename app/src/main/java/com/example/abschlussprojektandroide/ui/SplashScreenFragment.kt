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
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Beobachte das LiveData-Objekt für das Zitat des Tages
        viewModel.quoteOfTheDay.observe(viewLifecycleOwner) { quote ->
            // Überprüfe, ob das Zitat nicht null ist, bevor du es anzeigst
            quote?.let {
                binding.tvApiCallQuote.text = "\"${it.quote}\" - ${it.author}"
            }
        }


        // Lade das Zitat des Tages, wenn das ViewModel noch keinen Wert hat
        if (viewModel.quoteOfTheDay.value == null) {
            viewModel.loadQuoteOfTheDay()
        }

        /*
        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
        },5000)

         */
    }


}