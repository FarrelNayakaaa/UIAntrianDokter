package com.example.uiantriandokter.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.R

class AdminProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_admin_profile, container, false)
        val tv = v.findViewById<TextView>(R.id.tvAdminProfile)
        tv.text = "Admin\nNama: Administrator\nRole: Admin Klinik"
        return v
    }
}
