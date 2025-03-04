package com.example.maybefirstalready

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val buttonIntents = view.findViewById<Button>(R.id.button_intents)
        val buttonForegroundService = view.findViewById<Button>(R.id.button_foreground_service)
        val buttonBroadcastReceiver = view.findViewById<Button>(R.id.button_broadcast_receiver)
        val buttonContentProvider = view.findViewById<Button>(R.id.button_content_provider)

        buttonIntents.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_intentsFragment)
        }

        buttonForegroundService.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_foregroundServiceFragment)
        }

        buttonBroadcastReceiver.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_broadcastReceiverFragment)
        }

        buttonContentProvider.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_contentProviderFragment)
        }

        return view
    }
}