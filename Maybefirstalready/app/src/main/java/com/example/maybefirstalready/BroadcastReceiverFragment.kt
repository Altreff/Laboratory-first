package com.example.maybefirstalready

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class BroadcastReceiverFragment : Fragment() {

    private lateinit var statusTextView: TextView
    private val airplaneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                val isEnabled = intent.getBooleanExtra("state", false)
                val message = if (isEnabled) "Режим 'В самолете' Включен" else "Режим 'В самолете' Выключен"

                // Обновляем TextView
                statusTextView.text = message

                // Показываем Toast
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_broadcast_receiver, container, false)
        statusTextView = view.findViewById(R.id.airplaneModeStatus)
        return view
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        requireContext().registerReceiver(airplaneModeReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(airplaneModeReceiver)
    }
}
