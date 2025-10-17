package com.example.uiantriandokter.dokter.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.uiantriandokter.databinding.ActivityDoctorDetailBinding
import com.example.uiantriandokter.dokter.utils.DataDummy

class DoctorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorDetailBinding
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        index = intent.getIntExtra("index", -1)
        val patient = DataDummy.patientList[index]

        binding.tvName.text = patient.name
        binding.tvAge.text = "Umur: ${patient.age}"
        binding.tvComplaint.text = "Keluhan: ${patient.complaint}"
        binding.tvStatus.text = "Status: ${patient.status}"

        binding.btnEdit.setOnClickListener {
            Toast.makeText(this, "Edit pasien ${patient.name}", Toast.LENGTH_SHORT).show()
        }

        binding.btnPutLast.setOnClickListener {
            val moved = DataDummy.patientList.removeAt(index)
            DataDummy.patientList.add(moved)
            Toast.makeText(this, "Pasien dipindah ke akhir antrian", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Pasien?")
                .setMessage("Yakin ingin menghapus ${patient.name}?")
                .setPositiveButton("Ya") { _, _ ->
                    DataDummy.patientList.removeAt(index)
                    Toast.makeText(this, "Pasien dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }
}
