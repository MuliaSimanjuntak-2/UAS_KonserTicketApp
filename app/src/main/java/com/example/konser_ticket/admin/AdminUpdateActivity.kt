package com.example.konser_ticket.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konser_ticket.databinding.ActivityAdminUpdateBinding
import com.example.konser_ticket.model.Konser
import com.example.konser_ticket.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil data dari Intent
        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val artis = intent.getStringExtra("artis")
        val location = intent.getStringExtra("location")
        val price = intent.getStringExtra("price")
        val date = intent.getStringExtra("date")
        val description = intent.getStringExtra("description")

        // Mengisi EditText dengan data yang sudah ada
        with(binding) {
            etTitle.setText(title)
            etArtis.setText(artis)
            etLocation.setText(location)
            etPrice.setText(price)
            etDate.setText(date)
            etDescription.setText(description)

            // Ketika tombol Simpan ditekan, simpan perubahan ke server
            btnSave.setOnClickListener {
                val updatedTitle = etTitle.text.toString()
                val updatedArtis = etArtis.text.toString()
                val updatedLocation = etLocation.text.toString()
                val updatedPrice = etPrice.text.toString()
                val updatedDate = etDate.text.toString()
                val updatedDescription = etDescription.text.toString()

                // Validasi input
                if (validateInput(
                        updatedTitle,
                        updatedArtis,
                        updatedLocation,
                        updatedDate,
                        updatedPrice,
                        updatedDescription
                    )
                ) {
                    if (id != null) {
                        updateKonser(
                            id,
                            updatedTitle,
                            updatedArtis,
                            updatedLocation,
                            updatedDate,
                            updatedPrice,
                            updatedDescription
                        )
                    } else {
                        Toast.makeText(
                            this@AdminUpdateActivity,
                            "ID konser tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@AdminUpdateActivity,
                        "Data tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateInput(
        title: String,
        artis: String,
        location: String,
        date: String,
        price: String,
        description: String
    ): Boolean {
        // Pastikan semua input tidak kosong
        return title.isNotEmpty() && artis.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()
    }

    private fun updateKonser(
        id: String,
        title: String,
        artis: String,
        location: String,
        date: String,
        price: String,
        description: String
    ) {
        val apiService = ApiClient.getInstance()
        val konser = Konser(
            id = id,
            title = title,
            artis = artis,
            location = location,
            date = date,
            price = price,
            description = description
        )

        val response = apiService.updateKonser(id, konser)
        response.enqueue(object : Callback<Konser> {
            override fun onResponse(call: Call<Konser>, response: Response<Konser>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AdminUpdateActivity,
                        "Konser berhasil diperbarui",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@AdminUpdateActivity, AdminMainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@AdminUpdateActivity,
                        "Gagal memperbarui konser",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Konser>, t: Throwable) {
                Toast.makeText(this@AdminUpdateActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
