package com.example.uiantriandokter.dokter.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uiantriandokter.databinding.ItemDoctorPatientBinding
import com.example.uiantriandokter.dokter.model.Patient
import com.example.uiantriandokter.dokter.ui.DoctorDetailActivity

class DoctorPatientAdapter(private val list: MutableList<Patient>) :
    RecyclerView.Adapter<DoctorPatientAdapter.ViewHolder>() {

    inner class ViewHolder(val bind: ItemDoctorPatientBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bindData(patient: Patient, position: Int) {
            bind.tvTitle.text = "Biodata Pasien"
            bind.tvName.text = "Nama : ${patient.name}"
            bind.tvAge.text = "Umur : ${patient.age}"
            bind.tvComplaint.text = "Keluhan : ${patient.complaint}"
            bind.tvStatus.text = "Status : ${patient.status}"
            bind.imgProfile.setImageResource(patient.photoRes)

            // Aksi tombol
            bind.btnViewDetail.setOnClickListener {
                val ctx = itemView.context
                val intent = Intent(ctx, DoctorDetailActivity::class.java)
                intent.putExtra("index", position)
                ctx.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDoctorPatientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position], position)
    }
}
