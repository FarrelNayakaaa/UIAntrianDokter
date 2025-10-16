package com.example.uiantriandokter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.roundToInt

class ScheduleFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val destination = LatLng(-6.2088, 106.8456) // Contoh lokasi dokter
    private val LOCATION_PERMISSION_REQUEST = 1

    private var queueNumber = 0
    private var distanceKm = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnTakeQueue = view.findViewById<Button>(R.id.btnTakeQueue)
        val queueBox = view.findViewById<LinearLayout>(R.id.queueInfoBox)
        val tvQueue = view.findViewById<TextView>(R.id.tvQueueNumber)
        val tvDistance = view.findViewById<TextView>(R.id.tvDistance)
        val btnDetail = view.findViewById<Button>(R.id.btnDetailQueue)

        btnTakeQueue.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST
                )
                return@setOnClickListener
            }

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val distance = FloatArray(1)
                    Location.distanceBetween(
                        location.latitude, location.longitude,
                        destination.latitude, destination.longitude, distance
                    )

                    distanceKm = (distance[0] / 1000).roundToInt()
                    queueNumber = (1..50).random()

                    // tampilkan box antrian
                    queueBox.visibility = View.VISIBLE
                    tvQueue.text = "Nomor Antrian Anda: $queueNumber"
                    tvDistance.text = "Jarak ke lokasi: $distanceKm km"
                } else {
                    showError("Tidak dapat menemukan lokasi Anda.")
                }
            }
        }

        btnDetail.setOnClickListener {
            showQueueDialog()
        }
    }

    private fun showQueueDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_queue_map, null)
        mapView = dialogView.findViewById(R.id.mapView)
        val btnClose = dialogView.findViewById<Button>(R.id.btnCloseMap)

        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showError(msg: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Gagal")
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.addMarker(MarkerOptions().position(destination).title("Lokasi Dokter"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 14f))
    }
}
