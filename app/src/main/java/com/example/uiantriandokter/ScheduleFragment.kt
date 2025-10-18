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
                saveLastPatientToPrefs(bundle) // 🔹 simpan ke SharedPreferences
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

        // 🔹 Saat fragment dibuka, tampilkan data antrian terbaru
        val currentQueue = QueueHelper.getCurrentQueue(requireContext())
        val estimation = QueueHelper.getEstimation(requireContext())

        view.findViewById<TextView>(R.id.tvCurrentQueue)?.text = "$currentQueue"
        view.findViewById<TextView>(R.id.tvLimitPatient)?.text = "$currentQueue/15"
        view.findViewById<TextView>(R.id.tvEstimation)?.text = "Estimasi Antrian: $estimation Menit"

        // 🔹 Restore antrian terakhir jika ada
        if (QueueHelper.hasQueue(requireContext())) {
            val savedName = QueueHelper.getPatientName(requireContext()) ?: "-"
            val savedQueue = QueueHelper.getCurrentQueue(requireContext())

            // tampilkan card/box antrian
            queueBox?.visibility = View.VISIBLE
            tvQueue?.text = "Nomor Antrian Anda: A$savedQueue\nNama: $savedName"

            // 🔹 ambil ulang bundle tersimpan agar bisa diklik lagi
            val restored = restoreLastPatientFromPrefs()
            if (restored != null) {
                lastPatientData = restored
            } else {
                val restoredBundle = Bundle().apply {
                    putString("name", savedName)
                    putInt("queueNumber", savedQueue)
                    putInt("estimation", QueueHelper.getEstimation(requireContext()))
                }
                lastPatientData = restoredBundle
            }
        }

        // Klik box antrian → tampilkan detail via dialog
        queueBox?.setOnClickListener {
            lastPatientData?.let { bundle ->
                showPatientDetailDialog(bundle)
            } ?: run {
                Toast.makeText(requireContext(), "Data pasien tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        // Cek apakah user sudah punya antrian tersimpan
        if (QueueHelper.hasQueue(requireContext())) {
            val savedName = QueueHelper.getPatientName(requireContext()) ?: "-"
            val savedQueue = QueueHelper.getCurrentQueue(requireContext())
            val savedEstimation = QueueHelper.getEstimation(requireContext())

            queueBox?.visibility = View.VISIBLE
            tvQueue?.text = "Nomor Antrian Anda: A$savedQueue\nNama: $savedName"

            view.findViewById<TextView>(R.id.tvCurrentQueue)?.text = "$savedQueue"
            view.findViewById<TextView>(R.id.tvLimitPatient)?.text = "$savedQueue/15"
            view.findViewById<TextView>(R.id.tvEstimation)?.text = "Estimasi Antrian: $savedEstimation Menit"
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

                // 🔹 Tambahkan antrian baru (mulai dari 1, bukan random)
                QueueHelper.increaseQueue(requireContext())

                // 🔹 Ambil nilai terbaru dari QueueHelper
                val currentQueue = QueueHelper.getCurrentQueue(requireContext())
                val estimation = QueueHelper.getEstimation(requireContext())

                queueNumber = currentQueue

                // 🔹 update tampilan
                queueBox?.visibility = View.VISIBLE
                val name = lastPatientData?.getString("patientName") ?: "-"
                QueueHelper.savePatientName(requireContext(), name)
                tvQueue?.text = "Nomor Antrian Anda: A$queueNumber\nNama: $name"

                // 🔹 update juga bagian atas fragment
                view?.findViewById<TextView>(R.id.tvCurrentQueue)?.text = "$currentQueue"
                view?.findViewById<TextView>(R.id.tvLimitPatient)?.text = "$currentQueue/15"
                view?.findViewById<TextView>(R.id.tvEstimation)?.text = "Estimasi Antrian: $estimation Menit"
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

        val rootView = view

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi")
            .setMessage("Apakah kamu yakin ingin menghapus antrian ini?")
            .setPositiveButton("Ya") { _, _ ->
                QueueHelper.decreaseQueue(requireContext())
                QueueHelper.clearPatientData(requireContext())
                clearLastPatientFromPrefs() // 🔹 hapus simpanan bundle

                rootView?.post {
                    val newQueue = QueueHelper.getCurrentQueue(requireContext())
                    val newEstimation = QueueHelper.getEstimation(requireContext())

                    rootView.findViewById<TextView>(R.id.tvCurrentQueue)?.text = "$newQueue"
                    rootView.findViewById<TextView>(R.id.tvLimitPatient)?.text = "$newQueue/15"
                    rootView.findViewById<TextView>(R.id.tvEstimation)?.text =
                        "Estimasi Antrian: $newEstimation Menit"
                }

                Toast.makeText(requireContext(), "Antrian berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
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

    // 🔹 Tambahan fungsi simpan/restore bundle pasien terakhir agar tidak hilang
    private fun saveLastPatientToPrefs(bundle: Bundle) {
        val prefs = requireContext().getSharedPreferences("patient_prefs", 0)
        prefs.edit().apply {
            putString("patientName", bundle.getString("patientName"))
            putString("nik", bundle.getString("nik"))
            putString("age", bundle.getString("age"))
            putString("gender", bundle.getString("gender"))
            putString("phone", bundle.getString("phone"))
            putString("complaint", bundle.getString("complaint"))
        }.apply()
    }

    private fun restoreLastPatientFromPrefs(): Bundle? {
        val prefs = requireContext().getSharedPreferences("patient_prefs", 0)
        val name = prefs.getString("patientName", null) ?: return null
        return Bundle().apply {
            putString("patientName", name)
            putString("nik", prefs.getString("nik", "-"))
            putString("age", prefs.getString("age", "-"))
            putString("gender", prefs.getString("gender", "-"))
            putString("phone", prefs.getString("phone", "-"))
            putString("complaint", prefs.getString("complaint", "-"))
        }
    }

    private fun clearLastPatientFromPrefs() {
        val prefs = requireContext().getSharedPreferences("patient_prefs", 0)
        prefs.edit().clear().apply()
    }
}
