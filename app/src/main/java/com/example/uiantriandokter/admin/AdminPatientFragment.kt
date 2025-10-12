package com.example.uiantriandokter.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uiantriandokter.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminPatientFragment : Fragment() {

    private val patients = mutableListOf(
        AdminPatient("P001", "Budi Santoso", "08:30"),
        AdminPatient("P002", "Siti Aminah", "08:45")
    )
    private lateinit var adapter: AdminPatientAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_admin_patient, container, false)

        val rv = v.findViewById<RecyclerView>(R.id.rvAdminPatients)
        val fab = v.findViewById<FloatingActionButton>(R.id.fabAddPatient)

        adapter = AdminPatientAdapter(patients) { patient ->
            AlertDialog.Builder(requireContext())
                .setTitle("Hapus Antrian")
                .setMessage("Hapus ${patient.name} dari antrian?")
                .setPositiveButton("Hapus") { _, _ ->
                    adapter.removePatient(patient)
                    Toast.makeText(requireContext(), "Antrian dihapus", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        fab.setOnClickListener {
            val et = EditText(requireContext())
            et.hint = "Nama pasien"
            AlertDialog.Builder(requireContext())
                .setTitle("Tambah Antrian")
                .setView(et)
                .setPositiveButton("Tambah") { _, _ ->
                    val name = et.text.toString().trim()
                    if (name.isNotEmpty()) {
                        val newId = "P" + (100 + patients.size).toString()
                        val newPatient = AdminPatient(newId, name, "-")
                        adapter.addPatient(newPatient)
                    } else {
                        Toast.makeText(requireContext(), "Nama kosong", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        return v
    }
}
