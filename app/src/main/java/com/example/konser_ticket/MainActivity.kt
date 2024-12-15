package com.example.konser_ticket

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konser_ticket.databinding.ActivityMainBinding
import com.example.konser_ticket.login.LoginActivity
import com.example.konser_ticket.login.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnLogin.setOnClickListener{
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            btnRegister.setOnClickListener{
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            }
        }
    }
}