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

        // Update antrian & estimasi dari data global
        val tvQueueNo = view.findViewById<TextView>(R.id.tvQueueNo)
        val tvLimit = view.findViewById<TextView>(R.id.tvLimit)
        val tvEstimasi = view.findViewById<TextView>(R.id.tvEstimasi) // nanti kita tambahkan id ini di XML

        val currentQueue = QueueHelper.getCurrentQueue(requireContext())
        val estimation = QueueHelper.getEstimation(requireContext())

        tvQueueNo.text = "$currentQueue"
        tvLimit.text = "$currentQueue/15"
        tvEstimasi.text = "Estimasi Antrian $estimation Menit..."

        val fabMessage = view.findViewById<View>(R.id.fabMessage)
        val chatBox = view.findViewById<View>(R.id.chatBox)
        val btnCloseChat = view.findViewById<View>(R.id.btnCloseChat)
        val chatMessages = view.findViewById<TextView>(R.id.chatMessages)
        val optionKlinik = view.findViewById<TextView>(R.id.optionKlinik)
        val optionJadwal = view.findViewById<TextView>(R.id.optionJadwal)
        val optionAdmin = view.findViewById<TextView>(R.id.optionAdmin)

    // Awalnya sembunyikan
        chatBox.visibility = View.GONE

    // Saat tombol FAB ditekan
        fabMessage.setOnClickListener {
            chatBox.visibility = View.VISIBLE
            chatMessages.text = "Halo! 👋 Ada yang bisa kami bantu?\n\nPilih salah satu opsi di bawah:"
        }

    // Tombol close
        btnCloseChat.setOnClickListener {
            chatBox.visibility = View.GONE
        }

    // Respons chatbot sederhana
        optionKlinik.setOnClickListener {
            chatMessages.text = "🏥 Klinik kami buka setiap hari Senin–Sabtu pukul 08.00–20.00.\n\nAda lagi yang ingin ditanyakan?"
        }

        optionJadwal.setOnClickListener {
            chatMessages.text = "📅 Untuk melihat jadwal dokter, silakan buka menu *Schedule* pada tab bawah aplikasi."
        }

        optionAdmin.setOnClickListener {
            chatMessages.text = "🔗 Menghubungkan Anda ke Admin/Dokter...\nSilakan tunggu beberapa saat."
            // TODO: di sini nanti bisa diarahkan ke WhatsApp atau ChatFragment
        }

        return view
    }
}
