package com.example.uiantriandokter.dokter.adapter

import android.content.Intent
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.uiantriandokter.databinding.ItemDoctorPatientBinding
import com.example.uiantriandokter.dokter.model.Patient
import com.example.uiantriandokter.dokter.ui.DoctorDetailActivity

class DoctorPatientAdapter(private val list: MutableList<Patient>) :
    RecyclerView.Adapter<DoctorPatientAdapter.ViewHolder>() {

    inner class ViewHolder(val bind: ItemDoctorPatientBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDoctorPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = list[position]
        holder.bind.tvName.text = p.name
        holder.bind.tvComplaint.text = p.complaint
        holder.bind.tvStatus.text = "Status: ${p.status}"
        holder.bind.btnView.setOnClickListener {
            val ctx = holder.itemView.context
            val intent = Intent(ctx, DoctorDetailActivity::class.java)
            intent.putExtra("index", position)
            ctx.startActivity(intent)
        }
    }
}
