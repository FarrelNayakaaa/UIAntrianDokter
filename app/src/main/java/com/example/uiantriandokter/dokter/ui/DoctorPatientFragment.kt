package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.databinding.FragmentDoctorPatientBinding

class DoctorPatientFragment : Fragment() {
    private lateinit var binding: FragmentDoctorPatientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorPatientBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Tombol confirm dengan validasi
        binding.btnConfirm.setOnClickListener {
            val openHour = binding.timeOpen.hour
            val openMinute = binding.timeOpen.minute
            val closeHour = binding.timeClose.hour
            val closeMinute = binding.timeClose.minute

            val openTotal = openHour * 60 + openMinute
            val closeTotal = closeHour * 60 + closeMinute

            val valid = when {
                openTotal == closeTotal -> false // tidak boleh sama
                openTotal > closeTotal -> false // opening tidak boleh lebih lambat
                closeTotal < openTotal -> false // closing tidak boleh lebih cepat
                else -> true
            }

            if (valid) {
                Toast.makeText(requireContext(), "Waktu buka dan tutup telah diatur", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Waktu buka dan tutup invalid coba lagi", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
