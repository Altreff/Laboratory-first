package com.example.maybefirstalready

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat

class MyForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startMusic()
            ACTION_PAUSE -> pauseMusic()
            ACTION_STOP -> stopSelf() // Останавливаем сервис
        }

        return START_STICKY
    }

    private fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music)
            mediaPlayer?.isLooping = true
        }
        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
            updateNotification() // Обновляем уведомление
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            updateNotification() // Обновляем уведомление
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun updateNotification() {
        val startIntent = Intent(this, MyForegroundService::class.java).setAction(ACTION_START)
        val pauseIntent = Intent(this, MyForegroundService::class.java).setAction(ACTION_PAUSE)
        val stopIntent = Intent(this, MyForegroundService::class.java).setAction(ACTION_STOP)

        val startPendingIntent = PendingIntent.getService(this, 1, startIntent, PendingIntent.FLAG_IMMUTABLE)
        val pausePendingIntent = PendingIntent.getService(this, 2, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
        val stopPendingIntent = PendingIntent.getService(this, 3, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText(if (mediaPlayer?.isPlaying == true) "Playing music..." else "Paused")
            .setSmallIcon(R.drawable.ic_music)
            .addAction(NotificationCompat.Action(R.drawable.ic_play, "Play", startPendingIntent))
            .addAction(NotificationCompat.Action(R.drawable.ic_pause, "Pause", pausePendingIntent))
            .addAction(NotificationCompat.Action(R.drawable.ic_stop, "Stop", stopPendingIntent))
            .setOngoing(mediaPlayer?.isPlaying == true) // Блокировка свайпа при воспроизведении
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Music Player Service",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
