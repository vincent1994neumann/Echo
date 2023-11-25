package com.example.abschlussprojektandroide
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.abschlussprojektandroide.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Hier prüfen Sie die ID des aktuellen Fragments
            when (destination.id) {
                R.id.splashScreenFragment, R.id.surveyCreateFragment,R.id.loginFragment,R.id.registrierungFragment -> {
                    // BottomNavigationView für bestimmte Fragments ausblenden
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    // BottomNavigationView für alle anderen Fragments anzeigen
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }
}
