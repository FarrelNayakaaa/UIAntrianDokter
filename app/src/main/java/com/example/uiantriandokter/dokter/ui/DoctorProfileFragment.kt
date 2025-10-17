package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.databinding.FragmentDoctorProfileBinding

class DoctorProfileFragment : Fragment() {
    private lateinit var binding: FragmentDoctorProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDoctorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
}
