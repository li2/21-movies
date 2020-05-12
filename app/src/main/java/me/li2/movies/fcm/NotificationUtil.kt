package me.li2.movies.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.bumptech.glide.Glide

object NotificationUtil {
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                    NotificationChannel("channel_id_0", "COVID-19 News", NotificationManager.IMPORTANCE_HIGH),
                    NotificationChannel("channel_id_1", "Upcoming Movies", NotificationManager.IMPORTANCE_DEFAULT),
                    NotificationChannel("channel_id_2", "Cinemas News", NotificationManager.IMPORTANCE_DEFAULT)
            )
            context.getSystemService<NotificationManager>()?.let { notificationManager ->
                channels.forEach { channel ->
                    notificationManager.createNotificationChannel(channel)
                }
            }
        }
    }

    fun dispatchPushNotification(intent: Intent?, dispatcher: (MessageAPI) -> Unit) {
        val message = intent?.extras?.getParcelable<MessageAPI>(MessageService.EXTRA_KEY_MESSAGE)
        message?.let {
            dispatcher(it)
        }
    }
}


fun NotificationCompat.Builder.setBigImage(
        context: Context,
        imageUrl: Uri?,
        showThumbnail: Boolean = true): NotificationCompat.Builder {
    if (imageUrl == null) return this
    val bitmap = getImageBitmap(context, imageUrl)
    if (showThumbnail) {
        this.setLargeIcon(bitmap)
        this.setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null))
    } else {
        this.setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap))
    }
    return this
}

private fun getImageBitmap(context: Context, imageUrl: Uri): Bitmap {
    return Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .submit()
            .get()
}