package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.databinding.FragmentDoctorPatientBinding

class DoctorPatientFragment : Fragment() {

    private var _binding: FragmentDoctorPatientBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set default time (biar tampil sesuai rencana)
        binding.timeOpen.setIs24HourView(true)
        binding.timeClose.setIs24HourView(true)
        binding.timeOpen.hour = 10
        binding.timeOpen.minute = 0
        binding.timeClose.hour = 18
        binding.timeClose.minute = 0

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnConfirm.setOnClickListener {
            val openTotal = binding.timeOpen.hour * 60 + binding.timeOpen.minute
            val closeTotal = binding.timeClose.hour * 60 + binding.timeClose.minute

            if (openTotal < closeTotal) {
                Toast.makeText(requireContext(), "Waktu buka & tutup berhasil disimpan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Jam tidak valid. Coba atur lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
