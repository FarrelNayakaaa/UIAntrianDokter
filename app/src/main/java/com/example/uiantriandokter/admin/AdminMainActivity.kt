package com.example.uiantriandokter.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uiantriandokter.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.adminBottomNav)

        // default fragment
        replaceFragment(AdminPatientFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_patient -> replaceFragment(AdminPatientFragment())
                R.id.nav_schedule -> replaceFragment(AdminScheduleFragment())
                R.id.nav_report -> replaceFragment(AdminReportFragment())
                R.id.nav_profile -> replaceFragment(AdminProfileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.admin_nav_host_fragment, fragment)
            .commit()
    }
}
