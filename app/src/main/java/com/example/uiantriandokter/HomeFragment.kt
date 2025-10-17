package com.example.uiantriandokter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)

        val userData = SharedPrefHelper.getUserData(requireContext())
        val firstName = userData["firstName"]

        tvGreeting.text = "Hi, ${firstName ?: "User"} 👋"

        return view
    }
}
