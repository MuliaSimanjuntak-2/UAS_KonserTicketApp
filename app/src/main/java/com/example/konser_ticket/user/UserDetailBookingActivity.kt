package com.example.konser_ticket.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.konser_ticket.database.KonserDao
import com.example.konser_ticket.database.KonserDatabase
import com.example.konser_ticket.databinding.ActivityUserDetailBookingBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserDetailBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBookingBinding
    private lateinit var executorService: ExecutorService
    private lateinit var konserDao: KonserDao
    private var konserId: String? = null // ID konser yang akan dihapus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ExecutorService and DAO
        executorService = Executors.newSingleThreadExecutor()
        val db = KonserDatabase.getDatabase(applicationContext)
        konserDao = db!!.KonserDao()

        // Ambil ID konser dari Intent
        konserId = intent.getStringExtra("id")

        // Populate UI dengan data dari Intent
        with(binding) {
            tvTitle.text = intent.getStringExtra("title")
            tvDate.text = intent.getStringExtra("date")
            tvLocation.text = intent.getStringExtra("location")
            tvPrice.text = intent.getStringExtra("price")
            tvArtis.text = intent.getStringExtra("artis")
            tvDescription.text = intent.getStringExtra("description")

            ivBack.setOnClickListener {
                finish()
            }

            btnBatal.setOnClickListener {
                // Tampilkan dialog konfirmasi pembatalan
                showBatalConfirmationDialog()
            }
        }
    }

    private fun showBatalConfirmationDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Konfirmasi Pembatalan")
            .setMessage("Apakah Anda yakin ingin membatalkan tiket ini?")
            .setPositiveButton("Ya") { _, _ ->
                deleteFromRoom()
            }
            .setNegativeButton("Tidak", null)
            .create()
        dialog.show()
    }

    private fun deleteFromRoom() {
        executorService.execute {
            // Hapus data dari Room berdasarkan ID
            konserId?.let { id ->
                val konser = konserDao.getKonserById(id) // Ambil data berdasarkan ID
                konser?.let {
                    konserDao.delete(it)
                    runOnUiThread {
                        Toast.makeText(this, "Tiket berhasil dibatalkan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
