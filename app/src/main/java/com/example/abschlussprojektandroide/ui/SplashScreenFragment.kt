package com.example.abschlussprojektandroide.ui

import android.animation.ObjectAnimator
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
import com.google.firebase.auth.FirebaseAuth

class SplashScreenFragment : Fragment() {
    private lateinit var binding : FragmentSplashScreenBinding // Späte Initialisierung der Binding-Variable
    private val viewModel: SharedViewModel by activityViewModels() // Verwendung eines gemeinsamen ViewModel
    var firebaseAuth = FirebaseAuth.getInstance() // Instanz von FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false) // Inflate des Layouts für dieses Fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rotate()
        // Überprüft, ob ein Benutzer angemeldet ist und navigiert entsprechend
        if (firebaseAuth.currentUser == null){
            Handler(Looper.myLooper()!!).postDelayed({
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
            }, 6000) // Verzögerung von 4 Sekunden
        }

        // Lädt den aktuellen App-Benutzer und navigiert zum Home-Fragment, falls angemeldet
        viewModel.currentAppUser.observe(viewLifecycleOwner){
            if (it != null && viewModel.currentUser.value != null){
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
            }
        }

        // Überwacht Änderungen am aktuellen Firebase-Benutzer
        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.fetchCurrentUser()
            }
        }

        // Beobachtet das LiveData-Objekt für das Zitat des Tages
        viewModel.quoteOfTheDay.observe(viewLifecycleOwner) { quote ->
            // Setzt das Zitat des Tages im Textfeld
            quote?.let {
                binding.tvApiCallQuote.text = "\"${it.quote}\"\n - ${it.author} -"
            }
        }

        // Lädt das Zitat des Tages, falls es noch nicht geladen wurde
        if (viewModel.quoteOfTheDay.value == null) {
            viewModel.loadQuoteOfTheDay()
        }
    }

    private fun rotate (){
        val animation = ObjectAnimator.ofFloat(binding.imageView, View.ROTATION,0f,360f)
        animation.duration = 1500
        animation.repeatCount = 1
        animation.repeatMode = ObjectAnimator.REVERSE
        animation.start()
    }
}
