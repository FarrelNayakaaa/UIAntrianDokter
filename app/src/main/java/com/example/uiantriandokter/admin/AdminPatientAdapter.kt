package com.example.uiantriandokter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uiantriandokter.R

class AdminPatientAdapter(
    private val items: MutableList<AdminPatient>,
    private val onDelete: (AdminPatient) -> Unit
) : RecyclerView.Adapter<AdminPatientAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvPatientName)
        val tvId: TextView = view.findViewById(R.id.tvPatientId)
        val tvTime: TextView = view.findViewById(R.id.tvPatientTime)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeletePatient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_patient, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.tvName.text = p.name
        holder.tvId.text = p.id
        holder.tvTime.text = p.time
        holder.btnDelete.setOnClickListener {
            onDelete(p)
        }
    }

    override fun getItemCount(): Int = items.size

    // helper untuk menambah pasien
    fun addPatient(patient: AdminPatient) {
        items.add(0, patient)
        notifyItemInserted(0)
    }

    fun removePatient(patient: AdminPatient) {
        val idx = items.indexOf(patient)
        if (idx >= 0) {
            items.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }
}
