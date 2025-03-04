package com.example.maybefirstalready

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
class ContentProviderFragment : Fragment() {

    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var eventsAdapter: EventsAdapter
    private val eventsList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_content_provider, container, false)

        val requestPermissionButton = view.findViewById<Button>(R.id.requestPermissionButton)
        val loadEventsButton = view.findViewById<Button>(R.id.loadEventsButton)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        eventsAdapter = EventsAdapter(eventsList)
        recyclerView.adapter = eventsAdapter

        requestPermissionButton.setOnClickListener {
            checkPermission()
        }

        loadEventsButton.setOnClickListener {
            loadCalendarEvents()
        }

        return view
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.READ_CALENDAR),
                PERMISSION_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Разрешение уже предоставлено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Доступ к календарю разрешен", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Доступ запрещен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCalendarEvents() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Нет разрешения", Toast.LENGTH_SHORT).show()
            return
        }

        val projection = arrayOf(CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART)
        val cursor = requireContext().contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            null,
            null,
            CalendarContract.Events.DTSTART + " ASC"
        )

        eventsList.clear()

        cursor?.use {
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val dateIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex) ?: "Без названия"
                val date = it.getLong(dateIndex)
                val formattedDate = formatTimestamp(date) // Конвертируем дату

                eventsList.add("$title - $formattedDate") // Добавляем в список
            }
        }

        cursor?.close()
        eventsAdapter.notifyDataSetChanged()
    }

    // Метод для форматирования timestamp в строку
    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

}
