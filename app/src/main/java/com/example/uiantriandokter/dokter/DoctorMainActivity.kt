package com.example.uiantriandokter.dokter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.R
import com.example.uiantriandokter.databinding.ActivityDoctorMainBinding
import com.example.uiantriandokter.dokter.ui.DoctorHomeFragment
import com.example.uiantriandokter.dokter.ui.DoctorProfileFragment
import com.example.uiantriandokter.dokter.ui.DoctorScheduleFragment

class DoctorMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(DoctorHomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> loadFragment(DoctorHomeFragment())
                R.id.nav_schedule -> loadFragment(DoctorScheduleFragment())
                R.id.nav_profile -> loadFragment(DoctorProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
