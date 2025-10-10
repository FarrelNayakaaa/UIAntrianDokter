package com.example.uiantriandokter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btn = view.findViewById<Button>(R.id.btnTakeQueue)
        btn.setOnClickListener {
            Toast.makeText(requireContext(), "Tombol Ambil Antrian ditekan (UI statis)", Toast.LENGTH_SHORT).show()
        }
    }
}
