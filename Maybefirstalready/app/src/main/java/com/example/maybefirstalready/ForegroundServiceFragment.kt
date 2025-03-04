package com.example.maybefirstalready

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ForegroundServiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foreground_service, container, false)

        val startServiceButton = view.findViewById<Button>(R.id.startServiceButton)
        val pauseServiceButton = view.findViewById<Button>(R.id.pauseServiceButton)
        val stopServiceButton = view.findViewById<Button>(R.id.stopServiceButton)

        startServiceButton.setOnClickListener {
            sendCommandToService(MyForegroundService.ACTION_START)
        }

        pauseServiceButton.setOnClickListener {
            sendCommandToService(MyForegroundService.ACTION_PAUSE)
        }

        stopServiceButton.setOnClickListener {
            sendCommandToService(MyForegroundService.ACTION_STOP)
        }

        return view
    }

    private fun sendCommandToService(action: String) {
        val intent = Intent(requireContext(), MyForegroundService::class.java)
        intent.action = action
        requireContext().startService(intent)
    }
}
