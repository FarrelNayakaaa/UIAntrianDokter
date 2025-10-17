package com.example.uiantriandokter

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class RegisterPatientFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_patient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName = view.findViewById<EditText>(R.id.etName)
        val etNik = view.findViewById<EditText>(R.id.etNIK)
        val rgGender = view.findViewById<RadioGroup>(R.id.rgGender)
        val etBirthDate = view.findViewById<EditText>(R.id.etBirthDate)
        val etPhone = view.findViewById<EditText>(R.id.etPhone)
        val etAddress = view.findViewById<EditText>(R.id.etAddress)
        val etAllergy = view.findViewById<EditText>(R.id.etAllergy)
        val etComplaint = view.findViewById<EditText>(R.id.etComplaint)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmitRegister)

        val cal = Calendar.getInstance()

        // Date picker untuk tanggal lahir
        etBirthDate.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(), { _, y, m, d ->
                etBirthDate.setText("$d/${m + 1}/$y")
            }, year, month, day).show()
        }

        btnSubmit.setOnClickListener {
            val name = etName.text.toString().trim()
            val nik = etNik.text.toString().trim()
            val selectedGenderId = rgGender.checkedRadioButtonId
            val gender = if (selectedGenderId != -1)
                view.findViewById<RadioButton>(selectedGenderId).text.toString()
            else ""
            val birthDate = etBirthDate.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val allergy = etAllergy.text.toString().trim()
            val complaint = etComplaint.text.toString().trim()

            var hasError = false

            if (name.isEmpty()) {
                etName.error = "Nama wajib diisi"; hasError = true
            } else if (!name.matches(Regex("^[a-zA-Z\\s]+$"))) {
                etName.error = "Nama hanya boleh huruf"; hasError = true
            }

            if (nik.isEmpty()) {
                etNik.error = "NIK wajib diisi"; hasError = true
            } else if (!nik.matches(Regex("^\\d{16}$"))) {
                etNik.error = "NIK harus 16 digit"; hasError = true
            }

            if (selectedGenderId == -1) {
                Toast.makeText(requireContext(), "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                hasError = true
            }

            if (birthDate.isEmpty()) {
                etBirthDate.error = "Tanggal lahir wajib diisi"; hasError = true
            }

            if (phone.isEmpty()) {
                etPhone.error = "Nomor HP wajib diisi"; hasError = true
            } else if (!phone.matches(Regex("^\\d{10,13}$"))) {
                etPhone.error = "Nomor HP 10–13 digit"; hasError = true
            }

            if (address.isEmpty()) {
                etAddress.error = "Alamat wajib diisi"; hasError = true
            }

            if (complaint.isEmpty()) {
                etComplaint.error = "Keluhan wajib diisi"; hasError = true
            }

            if (hasError) return@setOnClickListener

            // Hitung usia berdasarkan tanggal lahir
            val age = calculateAge(birthDate)

            val result = Bundle().apply {
                putBoolean("isRegistered", true)
                putString("patientName", name)
                putString("nik", nik)
                putString("gender", gender)
                putString("birthDate", birthDate)
                putString("age", age.toString())
                putString("phone", phone)
                putString("address", address)
                putString("allergy", allergy)
                putString("complaint", complaint)
            }

            parentFragmentManager.setFragmentResult("registrationResult", result)
            Toast.makeText(requireContext(), "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun calculateAge(birthDateString: String): Int {
        return try {
            val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val birthDate = sdf.parse(birthDateString)
            val birthCal = Calendar.getInstance().apply { time = birthDate!! }
            val today = Calendar.getInstance()
            var age = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age
        } catch (e: Exception) {
            0
        }
    }
}
