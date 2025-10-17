package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uiantriandokter.R
import com.example.uiantriandokter.databinding.FragmentDoctorScheduleBinding
import com.example.uiantriandokter.dokter.adapter.DoctorPatientAdapter
import com.example.uiantriandokter.dokter.utils.DataDummy

class DoctorScheduleFragment : Fragment() {
    private var _binding: FragmentDoctorScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorScheduleBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        with(binding.rvPatients) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DoctorPatientAdapter(DataDummy.patientList)
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
