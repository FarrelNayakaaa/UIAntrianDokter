package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.databinding.FragmentDoctorHomeBinding
import com.example.uiantriandokter.dokter.utils.DataDummy
import com.example.uiantriandokter.R


class DoctorHomeFragment : Fragment() {
    private lateinit var binding: FragmentDoctorHomeBinding
    private var index = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDoctorHomeBinding.inflate(inflater, container, false)
        updateUI()

        binding.btnCall.setOnClickListener {
            if (DataDummy.patientList.isNotEmpty()) {
                DataDummy.patientList[index].status = "Called"
                Toast.makeText(requireContext(), "Pasien dipanggil", Toast.LENGTH_SHORT).show()
                binding.btnCall.isEnabled = false
                binding.btnCall.background = resources.getDrawable(R.drawable.bg_button_gray, null)
                updateUI()
            }
        }

        binding.btnDone.setOnClickListener {
            if (DataDummy.patientList.isNotEmpty()) {
                DataDummy.patientList[index].status = "Done"
                index++
                if (index >= DataDummy.patientList.size) {
                    binding.tvNoPatient.visibility = View.VISIBLE
                    binding.tvQueue.text = "Tidak ada pasien"
                } else {
                    Toast.makeText(requireContext(), "Antrian berikutnya", Toast.LENGTH_SHORT).show()
                    updateUI()
                }
            }
        }
        return binding.root
    }

    private fun updateUI() {
        if (index < DataDummy.patientList.size) {
            val p = DataDummy.patientList[index]
            binding.tvQueue.text = "Antrian Ke ${index + 1} dari ${DataDummy.patientList.size}"
            binding.btnCall.isEnabled = p.status == "Waiting"
            binding.tvNoPatient.visibility = View.GONE
        } else {
            binding.tvNoPatient.visibility = View.VISIBLE
        }
    }
}
