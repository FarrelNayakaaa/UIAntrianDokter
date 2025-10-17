package com.example.uiantriandokter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.roundToInt

class ScheduleFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val destination = LatLng(-6.2088, 106.8456)
    private val LOCATION_PERMISSION_REQUEST = 1

    private var queueNumber = 0
    private var distanceKm = 0

    // Views
    private var queueBox: LinearLayout? = null
    private var tvQueue: TextView? = null
    private var cardQueue: LinearLayout? = null

    // menyimpan data pasien terbaru (dari registrasi)
    private var lastPatientData: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnTakeQueue = view.findViewById<Button>(R.id.btnTakeQueue)
        queueBox = view.findViewById(R.id.queueInfoBox)
        tvQueue = view.findViewById(R.id.tvQueueNumber)
        cardQueue = view.findViewById(R.id.cardQueue)

        // Listener hasil registrasi dari RegisterPatientFragment
        parentFragmentManager.setFragmentResultListener("registrationResult", this) { _, bundle ->
            val isRegistered = bundle.getBoolean("isRegistered", false)
            if (isRegistered) {
                lastPatientData = bundle
                generateQueueAfterRegistration()
            }
        }

        // Tombol ambil antrian -> buka RegisterPatientFragment
        btnTakeQueue.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, RegisterPatientFragment())
                .addToBackStack(null)
                .commit()
        }

        // Klik box antrian → tampilkan detail via dialog
        queueBox?.setOnClickListener {
            lastPatientData?.let { bundle ->
                showPatientDetailDialog(bundle)
            }
        }
    }

    // fungsi yang dipanggil setelah registrasi sukses
    private fun generateQueueAfterRegistration() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val distance = FloatArray(1)
                Location.distanceBetween(
                    location.latitude, location.longitude,
                    destination.latitude, destination.longitude, distance
                )

                distanceKm = (distance[0] / 1000f).roundToInt()
                queueNumber = (1..50).random()

                queueBox?.visibility = View.VISIBLE
                val name = lastPatientData?.getString("patientName") ?: "-"
                tvQueue?.text = "Nomor Antrian Anda: A$queueNumber\nNama: $name"
            } else {
                showError("Tidak dapat menemukan lokasi Anda.")
            }
        }.addOnFailureListener {
            showError("Gagal mendapatkan lokasi: ${it.message}")
        }
    }

    private fun deleteQueue() {
        queueNumber = 0
        distanceKm = 0
        lastPatientData = null
        queueBox?.visibility = View.GONE
        tvQueue?.text = "Nomor Antrian: -"

        AlertDialog.Builder(requireContext())
            .setTitle("Dihapus")
            .setMessage("Antrian Anda telah dihapus.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showPatientDetailDialog(bundle: Bundle) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_patient_detail, null)
        val tvName = dialogView.findViewById<TextView>(R.id.tvPatientName)
        val tvDetail = dialogView.findViewById<TextView>(R.id.tvPatientDetail)
        val btnMap = dialogView.findViewById<Button>(R.id.btnViewMap)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDeleteQueue)

        val name = bundle.getString("patientName") ?: "-"
        val nik = bundle.getString("nik") ?: "-"
        val age = bundle.getString("age") ?: "-"
        val gender = bundle.getString("gender") ?: "-"
        val phone = bundle.getString("phone") ?: "-"
        val complaint = bundle.getString("complaint") ?: "-"

        tvName.text = name
        tvDetail.text = """
        NIK: $nik
        Usia: $age
        Jenis Kelamin: $gender
        Nomor HP: $phone

        Keluhan:
        $complaint
    """.trimIndent()

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnMap.setOnClickListener {
            dialog.dismiss()
            showQueueDialog()
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menghapus antrian ini?")
                .setPositiveButton("Ya, Hapus") { _, _ ->
                    dialog.dismiss()
                    deleteQueue()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun showQueueDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_queue_map, null)
        mapView = dialogView.findViewById(R.id.mapView)
        val btnClose = dialogView.findViewById<Button>(R.id.btnCloseMap)

        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync { gMap ->
            googleMap = gMap
            googleMap.addMarker(MarkerOptions().position(destination).title("Lokasi Dokter"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 14f))
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnClose.setOnClickListener { dialog.dismiss() }
        dialog.setOnDismissListener { mapView.onDestroy() }
        dialog.show()
    }

    private fun showError(msg: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Gagal")
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show()
    }
}
