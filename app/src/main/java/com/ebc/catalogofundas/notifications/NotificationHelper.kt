package com.ebc.catalogofundas.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ebc.catalogofundas.MainActivity
import com.ebc.catalogofundas.R

object NotificationHelper {

    const val CHANNEL_ID = "catalogo_fundas_channel"


    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Catálogo de Fundas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones del catálogo y favoritos"
            }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showStyleNotification(
        context: Context,
        openFavorites: Boolean
    ) {
        // Intent para abrir la app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("openFavorites", openFavorites)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            if (openFavorites) 200 else 201,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Sigue personalizando tu estilo!")
            .setContentText(
                "Tienes nuevas fundas disponibles en tu catálogo. " +
                        "Revisa tus favoritos y elige tu próximo diseño."
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context)
            .notify(if (openFavorites) 1 else 2, notification)
    }
}



