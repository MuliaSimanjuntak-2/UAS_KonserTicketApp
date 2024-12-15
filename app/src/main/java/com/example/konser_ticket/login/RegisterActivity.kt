package com.example.konser_ticket.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konser_ticket.databinding.ActivityRegisterBinding
import com.example.konser_ticket.model.User
import com.example.konser_ticket.network.ApiClient
import com.example.konser_ticket.prefManager.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        // Tombol Register
        binding.btnRegister.setOnClickListener {
            val name = binding.name.text.toString() // This is now 'name'
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.passwordConfirm.text.toString()
            val phone = binding.phone.text.toString() // Added phone input field
            val address = binding.address.text.toString() // Added address input field

            if (validateForm(name, email, password, confirmPassword, phone, address)) {
                // Kirim permintaan untuk mengecek apakah email sudah ada
                val apiService = ApiClient.getInstance()
                val response = apiService.getAllUsers()

                response.enqueue(object : Callback<List<User>> {
                    override fun onResponse(
                        call: Call<List<User>>,
                        response: Response<List<User>>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val users = response.body()
                            // Mengecek apakah email sudah terdaftar
                            val existingUser = users?.find { it.email == email }
                            if (existingUser != null) {
                                // Email sudah ada, beri tahu pengguna
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Email already exists",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Email belum ada, lanjutkan dengan pendaftaran
                                val user = User(
                                    id = null,
                                    name = name,
                                    email = email,
                                    password = password,
                                    phone = phone,
                                    address = address,
                                    role = "user" // Default role "user"
                                )
                                createUser(user)
                            }

                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Failed to fetch users",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please fill out the form correctly!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.loginPrompt.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk validasi form
    private fun validateForm(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String,
        address: String
    ): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                phone.isNotEmpty() && address.isNotEmpty() && password == confirmPassword
    }

    // Fungsi untuk membuat user baru
    private fun createUser(user: User) {
        val apiService = ApiClient.getInstance()
        val call = apiService.createUser(user)

        // Melakukan request API untuk membuat user
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // Jika berhasil, arahkan ke LoginActivity
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration Successful! please login again ",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    // Jika gagal
                    Toast.makeText(
                        this@RegisterActivity,
                        "Failed to register user",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Jika gagal terhubung ke API
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
