package com.example.konser_ticket.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konser_ticket.databinding.ActivityAdminMainBinding
import com.example.konser_ticket.login.LoginActivity
import com.example.konser_ticket.model.Konser
import com.example.konser_ticket.network.ApiClient
import com.example.konser_ticket.prefManager.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var prefManager: PrefManager
    private val listKonser = ArrayList<Konser>()
    private lateinit var adapter: AdminAdapater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        loadConcertData()
        setupRecyclerView()
        val createActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Muat ulang data baru dari server
                    loadConcertData()
                }
            }
        with(binding) {
            // Logout button with confirmation
            btnLogout.setOnClickListener {
                showLogoutConfirmation()
            }
            progresBar.visibility = android.view.View.VISIBLE

            btnCreate.setOnClickListener {
                val intent = Intent(this@AdminMainActivity, AdminCreateActivity::class.java)
                createActivityLauncher.launch(intent)
            }

        }

    }

    private fun setupRecyclerView() {
        adapter = AdminAdapater(listKonser, ApiClient.getInstance()) { data ->
            val intent = Intent(this, AdminDetailActivity::class.java).apply {
                putExtra("id", data.id)
                putExtra("title", data.title)
                putExtra("artis", data.artis)
                putExtra("location", data.location)
                putExtra("date", data.date)
                putExtra("price", data.price)
                putExtra("description", data.description)
            }
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadConcertData() {
        val apiService = ApiClient.getInstance()
        val call = apiService.getAllKonser()

        call.enqueue(object : Callback<List<Konser>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Konser>>, response: Response<List<Konser>>) {
                if (response.isSuccessful && response.body() != null) {
                    binding.progresBar.visibility = android.view.View.GONE
                    listKonser.clear()
                    listKonser.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@AdminMainActivity, "Failed to load concerts", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Konser>>, t: Throwable) {
                Toast.makeText(this@AdminMainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                prefManager.clear()
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java) // Ganti dengan activity login Anda
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
