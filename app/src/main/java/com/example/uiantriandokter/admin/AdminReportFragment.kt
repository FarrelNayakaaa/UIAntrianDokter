package com.example.uiantriandokter.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uiantriandokter.R

class AdminReportFragment : Fragment() {

    private val reports = mutableListOf(
        AdminReport("R001", "Budi Santoso", "Konsultasi - Selesai", "2025-10-01"),
        AdminReport("R002", "Siti Aminah", "Pemeriksaan - Selesai", "2025-10-02")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_admin_report, container, false)
        val rv = v.findViewById<RecyclerView>(R.id.rvAdminReports)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = AdminReportListAdapter(reports) { report ->
            // show detail dialog
            val layout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 24, 32, 24)
            }
            val tv1 = TextView(requireContext()).apply { text = "ID: ${report.id}" }
            val tv2 = TextView(requireContext()).apply { text = "Nama: ${report.patientName}" }
            val tv3 = TextView(requireContext()).apply { text = "Detail: ${report.detail}" }
            val tv4 = TextView(requireContext()).apply { text = "Tanggal: ${report.date}" }
            layout.addView(tv1); layout.addView(tv2); layout.addView(tv3); layout.addView(tv4)

            AlertDialog.Builder(requireContext())
                .setTitle("Detail Laporan")
                .setView(layout)
                .setPositiveButton("OK", null)
                .show()
        }
        return v
    }
}
