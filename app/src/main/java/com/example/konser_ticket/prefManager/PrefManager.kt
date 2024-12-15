package com.example.konser_ticket.prefManager

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {
    private val  sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_ADDRESS = "address"
        private const val KEY_PASSWORD = "password"
        private const val KEY_ROLE = "role"

        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUsername(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, name)
        editor.apply()
    }
    fun saveEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }
    fun savePhone(phone: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PHONE, phone)
        editor.apply()
    }
    fun saveAddress(address: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ADDRESS, address)
        editor.apply()
    }

    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun saveRole(role: String) { // Tambahkan metode untuk menyimpan role
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "") ?: ""
    }

    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }

    fun getRole(): String { // Tambahkan metode untuk mengambil role
        return sharedPreferences.getString(KEY_ROLE, "user") ?: "user"
    }
    fun getEmail(): String {
        return sharedPreferences.getString(KEY_EMAIL, "") ?: ""
    }
    fun getPhone(): String {
        return sharedPreferences.getString(KEY_PHONE, "") ?: ""
    }
    fun getAddress(): String {
        return sharedPreferences.getString(KEY_ADDRESS, "") ?: ""
    }


    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}