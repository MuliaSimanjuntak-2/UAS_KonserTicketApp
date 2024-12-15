package com.example.konser_ticket.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konser_ticket.R
import com.example.konser_ticket.databinding.ActivityAdminCreateBinding
import com.example.konser_ticket.model.Konser
import com.example.konser_ticket.network.ApiClient
import com.example.konser_ticket.network.ApiService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AdminCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminCreateBinding
    private var selectedDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val calendar = Calendar.getInstance()
        with(binding) {

            etDate.setOnClickListener {
                DatePickerDialog(
                    this@AdminCreateActivity,
                    { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        selectedDate =
                            format.format(calendar.time) // Konversi tanggal menjadi string
                        etDate.setText(selectedDate) // Tampilkan tanggal ke EditText
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            btnSave.setOnClickListener {
                val title = etTitle.text.toString()
                val location = etLocation.text.toString()
                val price = etPrice.text.toString()
                val artis = etArtis.text.toString()
                val description = etDescription.text.toString()

                if (validateInput(title, selectedDate, location, price, artis, description)) {
                    SaveToApi(title, selectedDate, location, price, artis, description)
                    //untuk mengriim data kemabil ke mainactivity
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this@AdminCreateActivity,
                        "Data tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }
    }

    private fun validateInput(
        title: String,
        date: String?,
        location: String,
        price: String,
        artis: String,
        description: String
    ): Boolean {
        return title.isNotEmpty() && date != null && location.isNotEmpty() && price.isNotEmpty() && artis.isNotEmpty() && description.isNotEmpty()
    }

    private fun SaveToApi(
        title: String,
        date: String,
        location: String,
        price: String,
        artis: String,
        description: String
    ) {
        val konser = Konser(
            title = title,
            date = date,
            location = location,
            price = price,
            artis = artis,
            description = description
        )
        val apiService = ApiClient.getInstance()
        val response = apiService.createKonser(konser)
        response.enqueue(object : retrofit2.Callback<Konser> {
            override fun onResponse(
                call: retrofit2.Call<Konser>,
                response: retrofit2.Response<Konser>
            ) {
                if (response.isSuccessful) {
                    // Jika request sukses, tampilkan pesan sukses
                    // dan kembali ke halaman sebelumnya
                    Toast.makeText(
                        this@AdminCreateActivity,
                        "Konser berhasil dibuat",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    // Jika request gagal, tampilkan pesan gagal
                    Toast.makeText(
                        this@AdminCreateActivity,
                        "Gagal membuat konser",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Konser>, t: Throwable) {
                // Jika terjadi error, tampilkan pesan error
                Toast.makeText(this@AdminCreateActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })


    }
}