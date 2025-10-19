package com.example.uiantriandokter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.uiantriandokter.dokter.DoctorMainActivity

class LoginActivity : AppCompatActivity() {

    // 🔹 Dummy akun dokter
    private val doctorAccounts = mapOf(
        "dokter1@gmail.com" to "dokter123",
        "dr.jane@gmail.com" to "Jane123"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvSignUp)

        // 🔹 Efek opacity dinamis
        fun applyAlphaWatcher(editText: EditText) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    editText.alpha = if (s.isNullOrEmpty()) 0.5f else 1.0f
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        applyAlphaWatcher(etEmail)
        applyAlphaWatcher(etPassword)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validasi input
            if (email.isEmpty()) {
                etEmail.error = "Masukkan email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword.error = "Masukkan password"
                return@setOnClickListener
            }

            // 🔹 Cek apakah akun dokter
            if (doctorAccounts.containsKey(email) && doctorAccounts[email] == password) {
                Toast.makeText(this, "Login sebagai Dokter", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DoctorMainActivity::class.java))
                finish()
                return@setOnClickListener
            }

            // 🔹 Jika bukan dokter → login sebagai user biasa
            Toast.makeText(this, "Login sebagai User", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
