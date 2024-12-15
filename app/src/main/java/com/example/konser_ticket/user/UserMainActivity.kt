package com.example.konser_ticket.user

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konser_ticket.R
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.konser_ticket.databinding.ActivityUserMainBinding
import com.example.konser_ticket.prefManager.PrefManager

class UserMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigationView.setupWithNavController(navController)

        }
    }
}