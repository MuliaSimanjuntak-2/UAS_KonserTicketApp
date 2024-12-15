package com.example.konser_ticket.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konser_ticket.admin.AdminMainActivity
import com.example.konser_ticket.databinding.ActivityLoginBinding
import com.example.konser_ticket.model.User
import com.example.konser_ticket.network.ApiClient
import com.example.konser_ticket.prefManager.PrefManager
import com.example.konser_ticket.user.UserMainActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        // If the user is already logged in, skip the login screen
        checkLoginStatus()
        // Handle login button click
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (validateForm(email, password)) {
                val apiService = ApiClient.getInstance()
                val response = apiService.getAllUsers()
                response.enqueue(object : Callback<List<User>> {
                    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                        if (response.isSuccessful && response.body() != null) {
                            response.body()?.forEach { i ->
                                if(i.email == email && i.password == password){
                                    prefManager.setLoggedIn(true)
                                    prefManager.saveRole(i.role)
                                    prefManager.saveUsername(i.name)
                                    prefManager.saveEmail(i.email)
                                    prefManager.savePhone(i.phone)
                                    prefManager.saveAddress(i.address)
                                    prefManager.savePassword(i.password)
                                    checkLoginStatus()
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show()
            }
        }
        binding.registerPrompt.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Validate form inputs
    private fun validateForm(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
    private fun checkLoginStatus(){
        if(prefManager.isLoggedIn()){
            if(prefManager.getRole() == "admin"){
                val intentToHome = Intent(this@LoginActivity, AdminMainActivity::class.java)
                startActivity(intentToHome)
            }else if(prefManager.getRole() == "user"){
                val intentToHome = Intent(this@LoginActivity,UserMainActivity::class.java)
                startActivity(intentToHome)
            }
        }
    }
}
