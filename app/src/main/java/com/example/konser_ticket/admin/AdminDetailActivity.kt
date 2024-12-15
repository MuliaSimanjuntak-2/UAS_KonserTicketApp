package com.example.konser_ticket.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konser_ticket.R
import com.example.konser_ticket.databinding.ActivityAdminDetailBinding

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            val title = intent.getStringExtra("title")
            val location = intent.getStringExtra("location")
            val date = intent.getStringExtra("date")
            val price = intent.getStringExtra("price")
            val artis = intent.getStringExtra("artis")
            val description = intent.getStringExtra("description")

            tvTitle.text = title
            tvLocation.text = "tempat:$location"
            tvDate.text = "tanggal:$date"
            tvPrice.text = "harga:$price"
            tvArtis.text = "artis:$artis"
            tvDescription.text = "deskripsi:$description"

            ivBack.setOnClickListener {
                finish()
            }
        }
    }
}