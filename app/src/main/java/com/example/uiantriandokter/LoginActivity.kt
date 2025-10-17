package com.example.uiantriandokter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
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
            val emailInput = etEmail.text.toString().trim()
            val passwordInput = etPassword.text.toString().trim()

            val savedEmail = SharedPrefHelper.getEmail(this)
            val savedPassword = SharedPrefHelper.getPassword(this)

            when {
                emailInput.isEmpty() -> etEmail.error = "Masukkan email"
                passwordInput.isEmpty() -> etPassword.error = "Masukkan password"
                emailInput != savedEmail || passwordInput != savedPassword ->
                    Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
