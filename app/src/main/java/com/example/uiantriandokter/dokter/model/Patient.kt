package com.example.uiantriandokter.dokter.model
data class Patient(
    val id: Int,
    var name: String,
    var age: Int,
    var complaint: String,
    var status: String, // Waiting, Ongoing, Called, Done
    var photoRes: Int
)
