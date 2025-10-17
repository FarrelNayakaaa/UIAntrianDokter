package com.example.uiantriandokter.dokter.utils

import com.example.uiantriandokter.R
import com.example.uiantriandokter.dokter.model.Patient

object DataDummy {
    val patientList = mutableListOf(
        Patient(1, "Felix Simatupang", 20, "Kecanduan Roblox", "Done", R.drawable.ic_profile),
        Patient(2, "Faiz Brembo", 19, "Keseringan jadi OP Starlight", "Ongoing", R.drawable.ic_profile),
        Patient(3, "EpanZ pake 2", 19, "Temenan ama Demon", "Waiting", R.drawable.ic_profile)
    )
}
