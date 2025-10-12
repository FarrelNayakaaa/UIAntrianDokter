package com.example.uiantriandokter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uiantriandokter.R

class AdminReportListAdapter(
    private val items: List<AdminReport>,
    private val onClick: (AdminReport) -> Unit
) : RecyclerView.Adapter<AdminReportListAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvReportTitle)
        val tvSub: TextView = view.findViewById(R.id.tvReportSub)
        val tvDate: TextView = view.findViewById(R.id.tvReportDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_report, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]
        holder.tvTitle.text = "${r.id} - ${r.patientName}"
        holder.tvSub.text = r.detail
        holder.tvDate.text = r.date
        holder.itemView.setOnClickListener { onClick(r) }
    }

    override fun getItemCount(): Int = items.size
}
