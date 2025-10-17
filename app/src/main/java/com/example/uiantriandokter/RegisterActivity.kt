package com.example.uiantriandokter

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirst = findViewById<EditText>(R.id.etFirstName)
        val etLast = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etBirth = findViewById<EditText>(R.id.etBirth)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val spGender = findViewById<Spinner>(R.id.spGender)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginHere = findViewById<TextView>(R.id.tvLoginHere)

        // === Inisialisasi kalender ===
        val calendar = Calendar.getInstance()

        // Nonaktifkan input manual untuk tanggal lahir
        etBirth.isFocusable = false
        etBirth.isClickable = true
        etBirth.isFocusableInTouchMode = false

        // === Tampilkan DatePicker saat diklik ===
        etBirth.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    etBirth.setText(format.format(selectedDate.time))
                },
                year, month, day
            )
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }

        // === Klik ikon kalender ===
        etBirth.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = etBirth.compoundDrawables[drawableEnd]
                if (drawable != null &&
                    event.rawX >= (etBirth.right - drawable.bounds.width())
                ) {
                    etBirth.performClick()
                    return@setOnTouchListener true
                }
            }
            false
        }

        etPassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = etPassword.compoundDrawables[drawableEnd]
                if (drawable != null &&
                    event.rawX >= (etPassword.right - drawable.bounds.width())
                ) {
                    togglePasswordVisibility(etPassword)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // === Tombol Register ===
        btnRegister.setOnClickListener {
            val firstName = etFirst.text.toString().trim()
            val lastName = etLast.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val birthDate = etBirth.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val gender = spGender.selectedItem.toString()

            when {
                firstName.isEmpty() -> etFirst.error = "Nama depan wajib diisi"
                email.isEmpty() -> etEmail.error = "Email wajib diisi"
                !email.contains("@") -> etEmail.error = "Format email tidak valid"
                password.length < 6 -> etPassword.error = "Password minimal 6 karakter"
                birthDate.isEmpty() -> etBirth.error = "Tanggal lahir wajib diisi"
                phone.isEmpty() -> etPhone.error = "Nomor telepon wajib diisi"
                else -> {
                    SharedPrefHelper.saveUser(
                        this,
                        firstName,
                        lastName,
                        email,
                        birthDate,
                        phone,
                        gender,
                        password
                    )
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

        // === Pindah ke halaman login ===
        tvLoginHere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // === Fungsi toggle show/hide password ===
    private fun togglePasswordVisibility(editText: EditText) {
        isPasswordVisible = !isPasswordVisible

        val drawable = if (isPasswordVisible)
            R.drawable.ic_eye_off   // 👁️‍🗨️ untuk hide
        else
            R.drawable.ic_eye        // 👁️ untuk show

        // ubah ikon di ujung EditText
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)

        // ubah tipe input
        if (isPasswordVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // pastikan kursor tetap di akhir teks
        editText.setSelection(editText.text.length)
    }
}
