package com.example.uiantriandokter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class ScheduleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btn = view.findViewById<Button>(R.id.btnTakeQueue2)
        btn.setOnClickListener {
            Toast.makeText(requireContext(), "Ambil Antrian (Schedule)", Toast.LENGTH_SHORT).show()
        }
    }
}
