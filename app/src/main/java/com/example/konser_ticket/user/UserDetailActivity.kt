package com.example.konser_ticket.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konser_ticket.R
import com.example.konser_ticket.database.KonserDao
import com.example.konser_ticket.database.KonserDatabase
import com.example.konser_ticket.database.KonserRoom
import com.example.konser_ticket.databinding.ActivityUserDetailBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var executorService: ExecutorService
    private lateinit var KonserDao: KonserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val konserRoomDatabase = KonserDatabase.getDatabase(applicationContext)
        KonserDao = konserRoomDatabase?.KonserDao()!!
        with(binding) {
            titleEvent.text = intent.getStringExtra("title")
            dateEvent.text = intent.getStringExtra("date")
            locationEvent.text = intent.getStringExtra("location")
            priceEvent.text = intent.getStringExtra("price")
            ArtisEvent.text = intent.getStringExtra("artis")
            descriptionEvent.text = intent.getStringExtra("description")

            ivBack.setOnClickListener {
                finish()
            }
            btnBuy.setOnClickListener {
                // Show confirmation dialog before buying
                showBuyConfirmationDialog()
            }
        }
    }

    private fun showBuyConfirmationDialog() {
        val konser = KonserRoom(
            id = intent.getStringExtra("id").toString(),
            title = intent.getStringExtra("title").toString(),
            date = intent.getStringExtra("date").toString(),
            location = intent.getStringExtra("location").toString(),
            price = intent.getStringExtra("price").toString(),
            artis = intent.getStringExtra("artis").toString(),
            description = intent.getStringExtra("description").toString()
        )

        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirm Purchase")
            .setMessage("Are you sure you want to buy this ticket for ${konser.title} at ${konser.location}?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                insert(konser)
            }
            .setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun insert(konser: KonserRoom) {
        executorService.execute {
            KonserDao.insert(konser)
            runOnUiThread {
                // Feedback to user after successful insertion
                AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Ticket for ${konser.title} has been added to your purchases.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }
}
