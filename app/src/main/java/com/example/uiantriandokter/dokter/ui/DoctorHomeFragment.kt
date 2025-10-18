package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.databinding.FragmentDoctorHomeBinding
import com.example.uiantriandokter.dokter.utils.DataDummy
import com.example.uiantriandokter.R
import com.google.android.material.snackbar.Snackbar

class DoctorHomeFragment : Fragment() {
    private lateinit var binding: FragmentDoctorHomeBinding
    private var index = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDoctorHomeBinding.inflate(inflater, container, false)
        updateUI()

        // ✅ HISTORY button
        binding.btnHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, DoctorHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        // ✅ NOTIFY button (simple Snackbar)
        binding.btnNotify.setOnClickListener {
            Snackbar.make(binding.root, "You have 3 new notifications!", Snackbar.LENGTH_SHORT).show()
        }

        // ✅ PATIENT button (open/close time)
        binding.btnPatient.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, DoctorPatientFragment())
                .addToBackStack(null)
                .commit()
        }

        // Call patient
        binding.btnCall.setOnClickListener {
            if (DataDummy.patientList.isNotEmpty() && index < DataDummy.patientList.size) {
                DataDummy.patientList[index].status = "Called"
                Toast.makeText(requireContext(), "Pasien dipanggil", Toast.LENGTH_SHORT).show()
                binding.btnCall.isEnabled = false
                binding.btnCall.background = resources.getDrawable(R.drawable.bg_button_gray, null)
                updateUI()
            }
        }

        // Close Queue button acts as "Done"
        binding.btnCloseQueue.setOnClickListener {
            if (DataDummy.patientList.isNotEmpty()) {
                DataDummy.patientList[index].status = "Done"
                index++
                if (index >= DataDummy.patientList.size) {
                    Toast.makeText(requireContext(), "Semua pasien sudah selesai", Toast.LENGTH_SHORT).show()
                    binding.tvPatientName.text = "Tidak ada pasien"
                    binding.tvQueueNumber.text = "-"
                    binding.btnCall.isEnabled = false
                    binding.btnCall.background = resources.getDrawable(R.drawable.bg_button_gray, null)
                } else {
                    Toast.makeText(requireContext(), "Pindah ke antrian berikutnya", Toast.LENGTH_SHORT).show()
                    updateUI()
                }
            }
        }

        // Open Queue just resets the patient index
        binding.btnOpenQueue.setOnClickListener {
            index = 0
            Toast.makeText(requireContext(), "Antrian dibuka kembali", Toast.LENGTH_SHORT).show()
            updateUI()
        }

        return binding.root
    }

    private fun updateUI() {
        if (DataDummy.patientList.isNotEmpty() && index < DataDummy.patientList.size) {
            val p = DataDummy.patientList[index]
            binding.tvPatientName.text = "${p.name} (${p.status})"
            binding.tvQueueNumber.text = "${index + 1}"
            binding.tvTotalPatients.text = "${DataDummy.patientList.size}"
            binding.btnCall.isEnabled = p.status == "Waiting"
            if (p.status == "Waiting") {
                binding.btnCall.background = resources.getDrawable(R.drawable.bg_button_green, null)
            } else {
                binding.btnCall.background = resources.getDrawable(R.drawable.bg_button_gray, null)
            }
        } else {
            binding.tvPatientName.text = "Tidak ada pasien"
            binding.tvQueueNumber.text = "-"
            binding.tvTotalPatients.text = "${DataDummy.patientList.size}"
            binding.btnCall.isEnabled = false
            binding.btnCall.background = resources.getDrawable(R.drawable.bg_button_gray, null)
        }
    }
}
