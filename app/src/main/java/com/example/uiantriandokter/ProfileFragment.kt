package com.example.uiantriandokter

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var tvNotificationStatus: TextView
    private var notificationsEnabled = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showNotification()
            } else {
                Toast.makeText(requireContext(), "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        tvNotificationStatus = view.findViewById(R.id.tvNotificationStatus)

        // ✅ Ambil dan tampilkan nama dari SharedPreferences
        val tvProfileName = view.findViewById<TextView>(R.id.profile_name)
        val firstName = SharedPrefHelper.getFirstName(requireContext())
        tvProfileName.text = firstName ?: "User"

        // Card Notifications toggle
        val cardNotif = view.findViewById<View>(R.id.card_notifications)
        cardNotif.setOnClickListener {
            notificationsEnabled = !notificationsEnabled
            updateNotificationStatus()
        }

        return view
    }

    private fun updateNotificationStatus() {
        tvNotificationStatus.text = if (notificationsEnabled) "ON" else "OFF"

        if (notificationsEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    return
                }
            }
            showNotification()
        } else {
            Toast.makeText(requireContext(), "Notifikasi dimatikan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNotification() {
        val channelId = "dokter_queue_channel"
        val notificationId = 101

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Dokter Queue Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Menampilkan notifikasi antrian dokter"
            }

            val manager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Antrian Dokter")
            .setContentText("Notifikasi aktif: Anda akan menerima pembaruan antrian.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        context?.let {
            with(NotificationManagerCompat.from(it)) {
                notify(notificationId, notification)
            }
        }
    }
}
