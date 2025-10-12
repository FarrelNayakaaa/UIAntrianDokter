package com.example.uiantriandokter.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.R

class AdminScheduleFragment : Fragment() {

    private var isOpen = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_admin_schedule, container, false)
        val switchOpen = v.findViewById<Switch>(R.id.switchOpenClose)
        val tvStatus = v.findViewById<TextView>(R.id.tvClinicStatus)
        val calendar = v.findViewById<CalendarView>(R.id.calendarView)

        switchOpen.isChecked = isOpen
        tvStatus.text = if (isOpen) "Klinik: BUKA" else "Klinik: TUTUP"

        switchOpen.setOnCheckedChangeListener { _, checked ->
            isOpen = checked
            tvStatus.text = if (isOpen) "Klinik: BUKA" else "Klinik: TUTUP"
        }

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val display = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            tvStatus.text = (if (isOpen) "Klinik: BUKA | " else "Klinik: TUTUP | ") + display
        }

        return v
    }
}
