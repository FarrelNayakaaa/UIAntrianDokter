package com.example.uiantriandokter.admin

data class AdminPatient(val id: String, val name: String, val time: String)
data class AdminReport(val id: String, val patientName: String, val detail: String, val date: String)
