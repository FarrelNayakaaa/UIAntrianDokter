package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.databinding.FragmentDoctorHistoryBinding
import com.example.uiantriandokter.dokter.utils.DataDummy
import com.example.uiantriandokter.R

class DoctorHistoryFragment : Fragment() {
    private lateinit var binding: FragmentDoctorHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorHistoryBinding.inflate(inflater, container, false)

        // Populate dummy data ke tabel
        populateTable()

        // Back button click
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    private fun populateTable() {
        val table = binding.tableHistory

        // Bersihkan tabel biar nggak dobel data pas refresh
        table.removeAllViews()

        // Header row
        val header = TableRow(requireContext())
        header.setBackgroundResource(R.color.gray)

        val headers = listOf("ID", "Name", "Age", "Complaint")
        for (title in headers) {
            val tvHeader = TextView(requireContext()).apply {
                text = title
                setPadding(16, 8, 16, 8)
                setTextAppearance(android.R.style.TextAppearance_Medium)
                setBackgroundResource(R.drawable.bg_table_cell_border)
                setTextColor(resources.getColor(R.color.black, null))
            }
            header.addView(tvHeader)
        }
        table.addView(header)

        // Data rows dari DataDummy
        for (patient in DataDummy.patientList) {
            val row = TableRow(requireContext())

            val tvId = makeCell(patient.id.toString())
            val tvName = makeCell(patient.name)
            val tvAge = makeCell(patient.age.toString())
            val tvComplaint = makeCell(patient.complaint)

            row.addView(tvId)
            row.addView(tvName)
            row.addView(tvAge)
            row.addView(tvComplaint)

            table.addView(row)
        }
    }

    private fun makeCell(text: String): TextView {
        return TextView(requireContext()).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            setBackgroundResource(R.drawable.bg_table_cell_border)
            setTextColor(resources.getColor(R.color.black, null))
        }
    }
}
